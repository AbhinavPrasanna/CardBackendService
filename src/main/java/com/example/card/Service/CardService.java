package com.example.card.Service;

import com.example.card.Model.Card;
import com.example.card.Repository.CardRepository;
import java.util.List;
import java.util.Optional;
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

    public Optional<Card> getCardByName(String cardName) {
        return cardRepository.findByCardName(cardName);
    }

    public List<Card> getCardsByType(String cardType) {
        return cardRepository.findByCardType(cardType);
    }

    public List<Card> getCardsByBank(String cardBank) {
        return cardRepository.findByCardBank(cardBank);
    }

    public List<Card> getCardsByHasAnnualFee(boolean hasAnnualFee) {
        return cardRepository.findByHasAnnualFee(hasAnnualFee);
    }

    public List<Card> getTop4ByRating() {
        return cardRepository.findTop4ByOrderByRatingDesc();
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
        cardToUpdate.setCreditScore(updatedCard.getCreditScore());

        return Optional.of(cardRepository.save(cardToUpdate));
    }
}
