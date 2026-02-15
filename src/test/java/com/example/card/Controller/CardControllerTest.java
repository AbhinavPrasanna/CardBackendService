package com.example.card.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.card.Model.Card;
import com.example.card.Service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CardService cardService;

    @Test
    void getAllCards_returnsOk() throws Exception {
        when(cardService.getAllCards()).thenReturn(List.of(buildCard("Card A", "Travel", "Bank A", 4.5)));

        mockMvc.perform(get("/card/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cardName").value("Card A"));
    }

    @Test
    void getCardById_whenFound_returnsOk() throws Exception {
        when(cardService.getCardById(1L)).thenReturn(Optional.of(buildCard("Card A", "Travel", "Bank A", 4.5)));

        mockMvc.perform(get("/card/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardName").value("Card A"));
    }

    @Test
    void getCardById_whenMissing_returnsNotFound() throws Exception {
        when(cardService.getCardById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/card/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTop4RatingCards_whenEmpty_returnsNotFound() throws Exception {
        when(cardService.getTop4ByRating()).thenReturn(List.of());

        mockMvc.perform(get("/card/top4-rating-cards"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addCard_returnsSavedCard() throws Exception {
        Card input = buildCard("Card B", "Cashback", "Bank B", 4.2);
        when(cardService.addCard(any(Card.class))).thenReturn(input);

        mockMvc.perform(post("/card/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardName").value("Card B"))
                .andExpect(jsonPath("$.cardType").value("Cashback"));
    }

    @Test
    void updateCardByName_whenFound_returnsOk() throws Exception {
        Card updated = buildCard("Updated", "Travel", "Bank X", 4.8);
        when(cardService.updateCardByName(eq("Old"), any(Card.class))).thenReturn(Optional.of(updated));

        mockMvc.perform(put("/card/update/Old")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardName").value("Updated"));
    }

    @Test
    void updateCardByName_whenMissing_returnsNotFound() throws Exception {
        Card updated = buildCard("Updated", "Travel", "Bank X", 4.8);
        when(cardService.updateCardByName(eq("Old"), any(Card.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/card/update/Old")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound());
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
