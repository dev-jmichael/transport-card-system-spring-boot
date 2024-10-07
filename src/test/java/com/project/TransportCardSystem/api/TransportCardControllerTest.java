package com.project.TransportCardSystem.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.TransportCardSystem.api.common.constants.TransportCardType;
import com.project.TransportCardSystem.application.transportcard.TransportCardService;
import com.project.TransportCardSystem.application.transportcard.dto.CreateTransportCardRequest;
import com.project.TransportCardSystem.application.transportcard.dto.TransportCardResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransportCardController.class)
public class TransportCardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransportCardService transportCardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateTransportCard() throws Exception {
        // Given
        CreateTransportCardRequest request = new CreateTransportCardRequest("Regular");
        TransportCardResponse response = new TransportCardResponse(
                1L,
                BigDecimal.valueOf(100.00),
                100,
                TransportCardType.REGULAR
        );

        when(transportCardService.createTransportCard(any(CreateTransportCardRequest.class)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/v1/transport-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.cardNumber").value(1L))
                .andExpect(jsonPath("$.data.cardType").value(TransportCardType.REGULAR.name()))
                .andExpect(jsonPath("$.data.loadAmount").value(100));
    }

    @Test
    public void testGetAllTransportCards() throws Exception {
        // Given
        TransportCardResponse card1 = new TransportCardResponse(
                1L,
                BigDecimal.valueOf(100.00),
                5,
                TransportCardType.REGULAR
        );
        TransportCardResponse card2 = new TransportCardResponse(
                2L,
                BigDecimal.valueOf(500.00),
                3,
                TransportCardType.DISCOUNTED
        );
        List<TransportCardResponse> cardList = Arrays.asList(card1, card2);

        when(transportCardService.getAllTransportCards()).thenReturn(cardList);

        // When & Then
        mockMvc.perform(get("/api/v1/transport-cards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].cardNumber").value(1L))
                .andExpect(jsonPath("$.data[0].cardType").value(TransportCardType.REGULAR.name()))
                .andExpect(jsonPath("$.data[0].loadAmount").value(100))
                .andExpect(jsonPath("$.data[1].cardNumber").value(2L))
                .andExpect(jsonPath("$.data[1].cardType").value(TransportCardType.DISCOUNTED.name()))
                .andExpect(jsonPath("$.data[1].loadAmount").value(500));
    }

    @Test
    public void testGetTransportCardById() throws Exception {
        // Given
        TransportCardResponse response = new TransportCardResponse(
                1L,
                BigDecimal.valueOf(100.00),
                100,
                TransportCardType.REGULAR
        );

        when(transportCardService.getTransportCardById(1L)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/v1/transport-cards/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.cardNumber").value(1L))
                .andExpect(jsonPath("$.data.cardType").value(TransportCardType.REGULAR.name()))
                .andExpect(jsonPath("$.data.loadAmount").value(100));
    }
}
