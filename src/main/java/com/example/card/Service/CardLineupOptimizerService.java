package com.example.card.Service;

import com.example.card.Graphql.CardLineupEntry;
import com.example.card.Graphql.CardLineupResult;
import com.example.card.Graphql.CategoryAssignment;
import com.example.card.Graphql.SpendingProfileInput;
import com.example.card.Model.Card;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CardLineupOptimizerService {

  private static final int LINEUP_SIZE = 5;

  private static final String[] CATEGORIES = {
    "travel",
    "dining",
    "grocery",
    "gas",
    "pharmacy",
    "lyft",
    "officeSupply",
    "services",
    "brand",
    "other"
  };

  private final CardService cardService;

  public CardLineupOptimizerService(CardService cardService) {
    this.cardService = cardService;
  }

  public CardLineupResult optimizeLineup(SpendingProfileInput input) {
    List<Card> allCards = cardService.getAllCards();

    List<Card> eligible =
        allCards.stream()
            .filter(card -> card.getCreditScore() <= input.getCreditScore())
            .collect(Collectors.toList());

    if (eligible.isEmpty()) {
      return new CardLineupResult(List.of(), 0, 0, 0, input.getTotalMonthlySpend());
    }

    Map<String, Double> monthlySpends = buildSpendMap(input);
    List<Card> selectedCards = greedySelect(eligible, monthlySpends, LINEUP_SIZE);

    return buildResult(selectedCards, monthlySpends, input.getTotalMonthlySpend());
  }

  private List<Card> greedySelect(
      List<Card> eligible, Map<String, Double> monthlySpends, int maxCards) {
    List<Card> selected = new ArrayList<>();
    Set<Long> selectedIds = new HashSet<>();
    double[] currentBestRates = new double[CATEGORIES.length];
    Arrays.fill(currentBestRates, 0.0);

    int limit = Math.min(maxCards, eligible.size());
    for (int i = 0; i < limit; i++) {
      Card bestCandidate = null;
      double bestMarginalValue = Double.NEGATIVE_INFINITY;

      for (Card candidate : eligible) {
        if (selectedIds.contains(candidate.getId())) {
          continue;
        }

        double marginalCashback = 0;
        for (int c = 0; c < CATEGORIES.length; c++) {
          double candidateRate = getEffectiveRate(candidate, CATEGORIES[c]);
          double improvement = candidateRate - currentBestRates[c];
          if (improvement > 0) {
            double spend = monthlySpends.getOrDefault(CATEGORIES[c], 0.0);
            marginalCashback += spend * (improvement / 100.0) * 12;
          }
        }

        double marginalNetValue =
            marginalCashback - (candidate.isHasAnnualFee() ? candidate.getAnnualFee() : 0);

        if (marginalNetValue > bestMarginalValue) {
          bestMarginalValue = marginalNetValue;
          bestCandidate = candidate;
        }
      }

      if (bestCandidate == null || bestMarginalValue <= 0) {
        break;
      }

      selected.add(bestCandidate);
      selectedIds.add(bestCandidate.getId());

      for (int c = 0; c < CATEGORIES.length; c++) {
        double rate = getEffectiveRate(bestCandidate, CATEGORIES[c]);
        currentBestRates[c] = Math.max(currentBestRates[c], rate);
      }
    }

    return selected;
  }

  private CardLineupResult buildResult(
      List<Card> selectedCards, Map<String, Double> monthlySpends, double totalMonthlySpend) {

    Map<Long, List<CategoryAssignment>> cardAssignments = new HashMap<>();
    for (Card card : selectedCards) {
      cardAssignments.put(card.getId(), new ArrayList<>());
    }

    double totalAnnualCashback = 0;

    for (String category : CATEGORIES) {
      double spend = monthlySpends.getOrDefault(category, 0.0);
      if (spend <= 0) {
        continue;
      }

      Card bestCard = null;
      double bestRate = 0;
      for (Card card : selectedCards) {
        double rate = getEffectiveRate(card, category);
        if (rate > bestRate) {
          bestRate = rate;
          bestCard = card;
        }
      }

      if (bestCard != null && bestRate > 0) {
        double annualCashback = spend * (bestRate / 100.0) * 12;
        totalAnnualCashback += annualCashback;
        cardAssignments
            .get(bestCard.getId())
            .add(new CategoryAssignment(category, spend, bestRate, annualCashback));
      }
    }

    int totalAnnualFees = 0;
    List<CardLineupEntry> entries = new ArrayList<>();
    for (Card card : selectedCards) {
      List<CategoryAssignment> assignments = cardAssignments.get(card.getId());
      double cardCashback = assignments.stream().mapToDouble(CategoryAssignment::getAnnualCashback).sum();
      int fee = card.isHasAnnualFee() ? card.getAnnualFee() : 0;
      totalAnnualFees += fee;
      entries.add(new CardLineupEntry(card, assignments, cardCashback, fee, cardCashback - fee));
    }

    double totalNetAnnualValue = totalAnnualCashback - totalAnnualFees;

    return new CardLineupResult(
        entries, totalAnnualCashback, totalAnnualFees, totalNetAnnualValue, totalMonthlySpend);
  }

  private double getEffectiveRate(Card card, String category) {
    double categoryRate =
        switch (category) {
          case "travel" -> card.getCashbackTravel();
          case "dining" -> card.getCashbackDining();
          case "grocery" -> card.getCashbackGrocery();
          case "gas" -> card.getCashbackGas();
          case "pharmacy" -> card.getCashbackPharmacy();
          case "lyft" -> card.getCashbackLyft();
          case "officeSupply" -> card.getCashbackOfficeSupply();
          case "services" -> card.getCashbackServices();
          case "brand" -> card.getCashbackBrand();
          case "other" -> card.getCashbackOther();
          default -> 0;
        };
    return Math.max(categoryRate, card.getCashbackFlat());
  }

  private Map<String, Double> buildSpendMap(SpendingProfileInput input) {
    Map<String, Double> spends = new HashMap<>();
    spends.put("travel", input.getMonthlyTravel());
    spends.put("dining", input.getMonthlyDining());
    spends.put("grocery", input.getMonthlyGrocery());
    spends.put("gas", input.getMonthlyGas());
    spends.put("pharmacy", input.getMonthlyPharmacy());
    spends.put("lyft", input.getMonthlyLyft());
    spends.put("officeSupply", input.getMonthlyOfficeSupply());
    spends.put("services", input.getMonthlyServices());
    spends.put("brand", input.getMonthlyBrand());
    spends.put("other", input.getMonthlyOther());
    return spends;
  }
}
