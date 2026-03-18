package com.example.card.Graphql;

import com.example.card.Model.Card;
import com.example.card.Service.CardService;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import reactor.core.publisher.Mono;

@Configuration
public class CardGraphqlDataLoaderConfig {

  public static final String CARDS_BY_BANK_DATA_LOADER = "cardsByBankDataLoader";

  public CardGraphqlDataLoaderConfig(BatchLoaderRegistry batchLoaderRegistry, CardService cardService) {
    batchLoaderRegistry
        .forTypePair(Long.class, Card.class)
        .registerMappedBatchLoader(
            (ids, environment) -> Mono.just(cardService.getCardsByIds(ids)));

    batchLoaderRegistry
        .<String, List<Card>>forName(CARDS_BY_BANK_DATA_LOADER)
        .registerMappedBatchLoader(
            (banks, environment) -> Mono.just(cardService.getCardsGroupedByBank(banks)));
  }
}
