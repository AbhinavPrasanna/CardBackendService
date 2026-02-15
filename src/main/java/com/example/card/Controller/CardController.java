package com.example.card.Controller;

import com.example.card.Model.Card;
import com.example.card.Repository.CardRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card")
public class CardController {

    private final CardRepository cardRepository;

    public CardController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok(cardRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable("id") Long id) {
        return cardRepository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Card> getCardByName(@PathVariable("name") String name) {
        return cardRepository.findByCardName(name).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/top4-rating-cards")
    public ResponseEntity<List<Card>> getTop4RatingCards() {
        List<Card> cards = cardRepository.findTop4ByOrderByRatingDesc();
        if (cards.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Card>> getCardByType(@PathVariable("type") String type) {
        List<Card> cards = cardRepository.findByCardType(type);
        if (cards.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/bank/{bank}")
    public ResponseEntity<List<Card>> getCardByBank(@PathVariable("bank") String bank) {
        List<Card> cards = cardRepository.findByCardBank(bank);
        if (cards.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/has-annual-fee/{hasAnnualFee}")
    public ResponseEntity<List<Card>> getCardByHasAnnualFee(@PathVariable("hasAnnualFee") boolean hasAnnualFee) {
        List<Card> cards = cardRepository.findByHasAnnualFee(hasAnnualFee);
        if (cards.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cards);
    }

    @PostMapping("/add")
    public ResponseEntity<Card> addCard(@RequestBody Card card) {
        Card savedCard = cardRepository.save(card);
        return ResponseEntity.ok(savedCard);
    }

    @PutMapping("/update/{name}")
    public ResponseEntity<Card> updateCardByName(@PathVariable("name") String name, @RequestBody Card updatedCard) {
        Optional<Card> existingCard = cardRepository.findByCardName(name);
        if (existingCard.isEmpty()) {
            return ResponseEntity.notFound().build();
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

        Card savedCard = cardRepository.save(cardToUpdate);
        return ResponseEntity.ok(savedCard);
    }
}