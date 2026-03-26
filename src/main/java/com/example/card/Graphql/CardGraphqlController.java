package com.example.card.Graphql;

import com.example.card.Model.Card;
import com.example.card.Service.CardLineupOptimizerService;
import com.example.card.Service.CardService;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.dataloader.DataLoader;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CardGraphqlController {

  private final CardService cardService;
  private final CardLineupOptimizerService cardLineupOptimizerService;

  public CardGraphqlController(
      CardService cardService, CardLineupOptimizerService cardLineupOptimizerService) {
    this.cardService = cardService;
    this.cardLineupOptimizerService = cardLineupOptimizerService;
  }

  @QueryMapping
  public CardPage cards(
      @Argument Integer page,
      @Argument Integer size,
      @Argument String bank,
      @Argument String type,
      @Argument Boolean hasAnnualFee) {
    int pageValue = page == null ? 0 : page;
    int sizeValue = size == null ? 20 : size;
    Page<Card> cardPage = cardService.getCardsPage(pageValue, sizeValue, bank, type, hasAnnualFee);
    return new CardPage(
        cardPage.getContent(),
        cardPage.getNumber(),
        cardPage.getSize(),
        cardPage.getTotalElements(),
        cardPage.getTotalPages(),
        cardPage.hasNext());
  }

  @QueryMapping
  public Card cardById(@Argument Long id) {
    return cardService.getCardById(id).orElse(null);
  }

  @QueryMapping
  public CompletableFuture<List<Card>> cardsByIds(
      @Argument List<Long> ids, DataLoader<Long, Card> cardDataLoader) {
    if (ids == null || ids.isEmpty()) {
      return CompletableFuture.completedFuture(List.of());
    }

    return cardDataLoader
        .loadMany(ids)
        .thenApply(cards -> cards.stream().filter(card -> card != null).collect(Collectors.toList()));
  }

  @QueryMapping
  public Card cardByName(@Argument String name) {
    return cardService.getCardByName(name).orElse(null);
  }

  @QueryMapping
  public List<Card> topRatedCards(@Argument Integer limit) {
    int safeLimit = limit == null ? 4 : limit;
    return cardService.getTopRatedCards(safeLimit);
  }

  @SchemaMapping(typeName = "Card", field = "relatedCardsByBank")
  public CompletableFuture<List<Card>> relatedCardsByBank(
      Card card, @Argument Integer limit, DataFetchingEnvironment environment) {
    if (card == null || card.getCardBank() == null || card.getCardBank().isBlank()) {
      return CompletableFuture.completedFuture(List.of());
    }

    int safeLimit = limit == null ? 5 : Math.max(1, limit);
    DataLoader<String, List<Card>> cardsByBankDataLoader =
        environment.getDataLoader(CardGraphqlDataLoaderConfig.CARDS_BY_BANK_DATA_LOADER);

    return cardsByBankDataLoader
        .load(card.getCardBank())
        .thenApply(
            cards ->
                cards.stream()
                    .filter(related -> related.getId() != card.getId())
                    .limit(safeLimit)
                    .collect(Collectors.toList()));
  }

  @QueryMapping
  public CardLineupResult optimizeCardLineup(@Argument SpendingProfileInput input) {
    return cardLineupOptimizerService.optimizeLineup(input);
  }

  @MutationMapping
  public Card addCard(@Argument CardInput input) {
    return cardService.addCard(input.toCard());
  }

  @MutationMapping
  public Card updateCardByName(@Argument String name, @Argument CardInput input) {
    return cardService.updateCardByName(name, input.toCard()).orElse(null);
  }

  public record CardPage(
      List<Card> content,
      int page,
      int size,
      long totalElements,
      int totalPages,
      boolean hasNext) {}

  public static class CardInput {
    public String cardName;
    public String cardType;
    public String cardBank;
    public boolean hasAnnualFee;
    public int annualFee;
    public double rating;
    public long bonus;
    public long bonusSpend;
    public double cashbackFlat;
    public double cashbackTravel;
    public double cashbackDining;
    public double cashbackGrocery;
    public double cashbackGas;
    public double cashbackPharmacy;
    public double cashbackLyft;
    public double cashbackOfficeSupply;
    public double cashbackServices;
    public double cashbackBrand;
    public double cashbackOther;
    public boolean cashbackChoiceHotels;
    public boolean cashbackHyattHotels;
    public boolean cashbackHiltonHotels;
    public boolean cashbackMarriottHotels;
    public boolean cashbackTravelIsHotelSpecific;
    public int creditScore;
    public String imageS3Key;
    public String imageSourceUrl;

    public Card toCard() {
      Card card = new Card();
      card.setCardName(cardName);
      card.setCardType(cardType);
      card.setCardBank(cardBank);
      card.setHasAnnualFee(hasAnnualFee);
      card.setAnnualFee(annualFee);
      card.setRating(rating);
      card.setBonus(bonus);
      card.setBonusSpend(bonusSpend);
      card.setCashbackFlat(cashbackFlat);
      card.setCashbackTravel(cashbackTravel);
      card.setCashbackDining(cashbackDining);
      card.setCashbackGrocery(cashbackGrocery);
      card.setCashbackGas(cashbackGas);
      card.setCashbackPharmacy(cashbackPharmacy);
      card.setCashbackLyft(cashbackLyft);
      card.setCashbackOfficeSupply(cashbackOfficeSupply);
      card.setCashbackServices(cashbackServices);
      card.setCashbackBrand(cashbackBrand);
      card.setCashbackOther(cashbackOther);
      card.setCashbackChoiceHotels(cashbackChoiceHotels);
      card.setCashbackHyattHotels(cashbackHyattHotels);
      card.setCashbackHiltonHotels(cashbackHiltonHotels);
      card.setCashbackMarriottHotels(cashbackMarriottHotels);
      card.setCashbackTravelIsHotelSpecific(cashbackTravelIsHotelSpecific);
      card.setCreditScore(creditScore);
      card.setImageS3Key(imageS3Key);
      card.setImageSourceUrl(imageSourceUrl);
      return card;
    }
  }
}
