package com.project.TransportCardSystem.api;

import com.project.TransportCardSystem.application.cardtransaction.CardTransactionService;
import com.project.TransportCardSystem.api.common.exceptions.InsufficientBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardTransactionController.class)
public class CardTransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;  // Inject MockMvc

    @MockBean
    private CardTransactionService service;  // Mock the service

    @BeforeEach
    public void setUp() {
        Mockito.reset(service);  // Reset mock before each test
    }

    @Test
    public void testDeductFare_Success() throws Exception {
        // Arrange
        Long cardNumber = 123L;
        String expectedResponse = "Current balance: 40.00";

        // Mock service response
        Mockito.when(service.processFareDeduction(cardNumber)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/transport-cards/{cardNumber}", cardNumber))
                .andExpect(status().isOk())  // Check for 200 OK status
                .andExpect(content().string(expectedResponse));  // Check the response content
    }

    @Test
    public void testDeductFare_InsufficientBalance() throws Exception {
        // Arrange
        Long cardNumber = 123L;
        Mockito.when(service.processFareDeduction(cardNumber))
                .thenThrow(new InsufficientBalanceException("Insufficient balance"));

        // Act & Assert
        mockMvc.perform(patch("/api/v1/transport-cards/{cardNumber}", cardNumber))
                .andExpect(status().isPaymentRequired());  // Expect 500 Internal Server Error
    }
}
