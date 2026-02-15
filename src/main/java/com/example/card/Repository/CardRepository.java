package com.example.card.Repository;

import com.example.card.Model.Card;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByCardName(String cardName);

    List<Card> findByCardType(String cardType);

    List<Card> findByCardBank(String cardBank);

    List<Card> findByHasAnnualFee(boolean hasAnnualFee);

    List<Card> findTop4ByOrderByRatingDesc();
}