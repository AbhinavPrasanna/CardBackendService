package com.example.card.Service;

import com.example.card.Model.Card;
import com.example.card.Repository.CardRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CardService {

  private final CardRepository cardRepository;

  public CardService(CardRepository cardRepository) {
    this.cardRepository = cardRepository;
  }

  public List<Card> getAllCards() {
    return cardRepository.findAll();
  }

  public Optional<Card> getCardById(Long id) {
    return cardRepository.findById(id);
  }

  public Map<Long, Card> getCardsByIds(Set<Long> ids) {
    if (ids == null || ids.isEmpty()) {
      return Map.of();
    }

    List<Card> cards = cardRepository.findAllById(ids);
    Map<Long, Card> cardsById = new LinkedHashMap<>();
    for (Card card : cards) {
      cardsById.put(card.getId(), card);
    }
    return cardsById;
  }

  public Optional<Card> getCardByName(String cardName) {
    return cardRepository.findByCardName(cardName);
  }

  public List<Card> getCardsByType(String cardType) {
    return cardRepository.findByCardType(cardType);
  }

  public List<Card> getCardsByBank(String cardBank) {
    return cardRepository.findByCardBank(cardBank);
  }

  public Map<String, List<Card>> getCardsGroupedByBank(Set<String> banks) {
    if (banks == null || banks.isEmpty()) {
      return Map.of();
    }

    Map<String, List<Card>> cardsByBank = new LinkedHashMap<>();
    for (String bank : banks) {
      cardsByBank.put(bank, new ArrayList<>());
    }

    List<Card> cards = cardRepository.findByCardBankIn(banks);
    for (Card card : cards) {
      cardsByBank.computeIfAbsent(card.getCardBank(), unused -> new ArrayList<>()).add(card);
    }

    return cardsByBank;
  }

  public List<Card> getCardsByHasAnnualFee(boolean hasAnnualFee) {
    return cardRepository.findByHasAnnualFee(hasAnnualFee);
  }

  public List<Card> getTop4ByRating() {
    return cardRepository.findTop4ByOrderByRatingDesc();
  }

  public List<Card> getTopRatedCards(int limit) {
    Pageable pageable = PageRequest.of(0, Math.max(1, limit));
    return cardRepository.findAll((root, query, cb) -> cb.conjunction(), pageable).getContent();
  }

  public Page<Card> getCardsPage(
      int page, int size, String bank, String type, Boolean hasAnnualFee) {
    int safePage = Math.max(0, page);
    int safeSize = Math.max(1, size);
    Pageable pageable = PageRequest.of(safePage, safeSize);

    Specification<Card> spec = Specification.where(null);
    if (bank != null && !bank.isBlank()) {
      spec = spec.and((root, query, cb) -> cb.equal(root.get("cardBank"), bank));
    }
    if (type != null && !type.isBlank()) {
      spec = spec.and((root, query, cb) -> cb.equal(root.get("cardType"), type));
    }
    if (hasAnnualFee != null) {
      spec = spec.and((root, query, cb) -> cb.equal(root.get("hasAnnualFee"), hasAnnualFee));
    }

    return cardRepository.findAll(spec, pageable);
  }

  public Card addCard(Card card) {
    return cardRepository.save(card);
  }

  public Optional<Card> updateCardByName(String name, Card updatedCard) {
    Optional<Card> existingCard = cardRepository.findByCardName(name);
    if (existingCard.isEmpty()) {
      return Optional.empty();
    }

    Card cardToUpdate = existingCard.get();
    cardToUpdate.setCardName(updatedCard.getCardName());
    cardToUpdate.setCardType(updatedCard.getCardType());
    cardToUpdate.setCardBank(updatedCard.getCardBank());
    cardToUpdate.setHasAnnualFee(updatedCard.isHasAnnualFee());
    cardToUpdate.setAnnualFee(updatedCard.getAnnualFee());
    cardToUpdate.setRating(updatedCard.getRating());
    cardToUpdate.setBonus(updatedCard.getBonus());
    cardToUpdate.setBonusSpend(updatedCard.getBonusSpend());
    cardToUpdate.setCashbackFlat(updatedCard.getCashbackFlat());
    cardToUpdate.setCashbackTravel(updatedCard.getCashbackTravel());
    cardToUpdate.setCashbackDining(updatedCard.getCashbackDining());
    cardToUpdate.setCashbackGrocery(updatedCard.getCashbackGrocery());
    cardToUpdate.setCashbackGas(updatedCard.getCashbackGas());
    cardToUpdate.setCashbackPharmacy(updatedCard.getCashbackPharmacy());
    cardToUpdate.setCashbackLyft(updatedCard.getCashbackLyft());
    cardToUpdate.setCashbackOfficeSupply(updatedCard.getCashbackOfficeSupply());
    cardToUpdate.setCashbackServices(updatedCard.getCashbackServices());
    cardToUpdate.setCashbackBrand(updatedCard.getCashbackBrand());
    cardToUpdate.setCashbackOther(updatedCard.getCashbackOther());
    cardToUpdate.setCashbackChoiceHotels(updatedCard.isCashbackChoiceHotels());
    cardToUpdate.setCashbackHyattHotels(updatedCard.isCashbackHyattHotels());
    cardToUpdate.setCashbackHiltonHotels(updatedCard.isCashbackHiltonHotels());
    cardToUpdate.setCashbackMarriottHotels(updatedCard.isCashbackMarriottHotels());
    cardToUpdate.setCashbackTravelIsHotelSpecific(updatedCard.isCashbackTravelIsHotelSpecific());
    cardToUpdate.setCreditScore(updatedCard.getCreditScore());

    return Optional.of(cardRepository.save(cardToUpdate));
  }
}
