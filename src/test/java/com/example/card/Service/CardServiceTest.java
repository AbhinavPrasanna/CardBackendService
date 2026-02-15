package com.example.card.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.card.Model.Card;
import com.example.card.Repository.CardRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    @Test
    void getAllCards_returnsRepositoryResults() {
        Card card = buildCard("Card A", "Travel", "Bank A", 4.5);
        when(cardRepository.findAll()).thenReturn(List.of(card));

        List<Card> result = cardService.getAllCards();

        assertEquals(1, result.size());
        assertEquals("Card A", result.get(0).getCardName());
        verify(cardRepository).findAll();
    }

    @Test
    void getCardByName_returnsOptionalWhenFound() {
        Card card = buildCard("Card B", "Cashback", "Bank B", 4.2);
        when(cardRepository.findByCardName("Card B")).thenReturn(Optional.of(card));

        Optional<Card> result = cardService.getCardByName("Card B");

        assertTrue(result.isPresent());
        assertEquals("Card B", result.get().getCardName());
        verify(cardRepository).findByCardName("Card B");
    }

    @Test
    void getTop4ByRating_returnsOrderedRepositoryResults() {
        Card first = buildCard("Top 1", "Travel", "Bank A", 5.0);
        Card second = buildCard("Top 2", "Travel", "Bank B", 4.9);
        when(cardRepository.findTop4ByOrderByRatingDesc()).thenReturn(List.of(first, second));

        List<Card> result = cardService.getTop4ByRating();

        assertEquals(2, result.size());
        assertEquals("Top 1", result.get(0).getCardName());
        verify(cardRepository).findTop4ByOrderByRatingDesc();
    }

    @Test
    void addCard_savesAndReturnsCard() {
        Card input = buildCard("New Card", "Rewards", "Bank C", 4.0);
        when(cardRepository.save(input)).thenReturn(input);

        Card result = cardService.addCard(input);

        assertEquals("New Card", result.getCardName());
        verify(cardRepository).save(input);
    }

    @Test
    void updateCardByName_returnsEmptyWhenCardDoesNotExist() {
        Card updatedCard = buildCard("Missing", "Travel", "Bank Z", 3.0);
        when(cardRepository.findByCardName("Missing")).thenReturn(Optional.empty());

        Optional<Card> result = cardService.updateCardByName("Missing", updatedCard);

        assertFalse(result.isPresent());
        verify(cardRepository).findByCardName("Missing");
    }

    @Test
    void updateCardByName_updatesExistingCardAndReturnsSavedCard() {
        Card existing = buildCard("Old Name", "Basic", "Bank A", 3.2);
        Card updated = buildCard("Updated Name", "Travel", "Bank X", 4.8);
        updated.setHasAnnualFee(true);
        updated.setAnnualFee(95);
        updated.setBonus(60000);
        updated.setBonusSpend(4000);
        updated.setCashbackFlat(1.5);
        updated.setCashbackTravel(3.0);
        updated.setCashbackDining(2.0);
        updated.setCashbackGrocery(1.0);
        updated.setCashbackGas(2.0);
        updated.setCashbackPharmacy(1.0);
        updated.setCashbackLyft(5.0);
        updated.setCashbackOfficeSupply(0.5);
        updated.setCashbackServices(1.2);
        updated.setCashbackBrand(2.5);
        updated.setCashbackOther(1.0);
        updated.setCreditScore(720);

        when(cardRepository.findByCardName("Old Name")).thenReturn(Optional.of(existing));
        when(cardRepository.save(existing)).thenReturn(existing);

        Optional<Card> result = cardService.updateCardByName("Old Name", updated);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getCardName());
        assertEquals("Travel", result.get().getCardType());
        assertEquals("Bank X", result.get().getCardBank());
        assertTrue(result.get().isHasAnnualFee());
        assertEquals(95, result.get().getAnnualFee());
        assertEquals(4.8, result.get().getRating());
        assertEquals(720, result.get().getCreditScore());
        verify(cardRepository).findByCardName("Old Name");
        verify(cardRepository).save(existing);
    }

    private Card buildCard(String name, String type, String bank, double rating) {
        Card card = new Card();
        card.setCardName(name);
        card.setCardType(type);
        card.setCardBank(bank);
        card.setRating(rating);
        return card;
    }
}
