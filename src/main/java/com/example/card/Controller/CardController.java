package com.example.card.Controller;

import com.example.card.Model.Card;
import com.example.card.Service.CardService;
import java.util.List;
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

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable("id") Long id) {
        return cardService.getCardById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Card> getCardByName(@PathVariable("name") String name) {
        return cardService.getCardByName(name).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/top4-rating-cards")
    public ResponseEntity<List<Card>> getTop4RatingCards() {
        List<Card> cards = cardService.getTop4ByRating();
        if (cards.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Card>> getCardByType(@PathVariable("type") String type) {
        List<Card> cards = cardService.getCardsByType(type);
        if (cards.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/bank/{bank}")
    public ResponseEntity<List<Card>> getCardByBank(@PathVariable("bank") String bank) {
        List<Card> cards = cardService.getCardsByBank(bank);
        if (cards.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/has-annual-fee/{hasAnnualFee}")
    public ResponseEntity<List<Card>> getCardByHasAnnualFee(@PathVariable("hasAnnualFee") boolean hasAnnualFee) {
        List<Card> cards = cardService.getCardsByHasAnnualFee(hasAnnualFee);
        if (cards.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cards);
    }

    @PostMapping("/add")
    public ResponseEntity<Card> addCard(@RequestBody Card card) {
        Card savedCard = cardService.addCard(card);
        return ResponseEntity.ok(savedCard);
    }

    @PutMapping("/update/{name}")
    public ResponseEntity<Card> updateCardByName(@PathVariable("name") String name, @RequestBody Card updatedCard) {
        return cardService.updateCardByName(name, updatedCard)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}