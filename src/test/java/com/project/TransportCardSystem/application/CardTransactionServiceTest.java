package com.project.TransportCardSystem.application;

import com.project.TransportCardSystem.application.cardtransaction.CardTransactionService;
import com.project.TransportCardSystem.api.common.constants.TransportCardType;
import com.project.TransportCardSystem.api.common.exceptions.InsufficientBalanceException;
import com.project.TransportCardSystem.domain.entities.TransportCard;
import com.project.TransportCardSystem.infrastructure.repositories.TransportCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CardTransactionServiceTest {
    @Mock
    private TransportCardRepository repository;

    @InjectMocks
    private CardTransactionService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

    @Test
    public void testProcessFareDeduction_Success() {
        // Arrange
        Long cardNumber = 123L;
        BigDecimal initialBalance = BigDecimal.valueOf(500.00);
        Integer yearsOfValidity = 5;
        TransportCard transportCard = new TransportCard(
                cardNumber,
                initialBalance,
                yearsOfValidity,
                TransportCardType.DISCOUNTED
        );

        when(repository.findById(cardNumber)).thenReturn(Optional.of(transportCard));
        when(repository.save(any(TransportCard.class))).thenReturn(transportCard);

        // Act
        String result = service.processFareDeduction(cardNumber);

        // Assert
        assertEquals("Current balance: 492.00", result);
        assertEquals(BigDecimal.valueOf(492.00).setScale(2), transportCard.getLoadAmount());
        verify(repository).save(transportCard);  // Ensure the repository save was called
    }

    @Test
    public void testProcessFareDeduction_InsufficientBalance() {
        // Arrange
        Long cardNumber = 123L;
        BigDecimal balance = BigDecimal.valueOf(10.00);
        Integer yearsOfValidity = 5;
        TransportCard transportCard = new TransportCard(
                cardNumber,
                balance,
                yearsOfValidity,
                TransportCardType.REGULAR
        );

        when(repository.findById(cardNumber)).thenReturn(Optional.of(transportCard));

        // Act & Assert
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> {
            service.processFareDeduction(cardNumber);
        });

        assertEquals("Insufficient balance on the card.", exception.getMessage());
        verify(repository, never()).save(any(TransportCard.class));  // Ensure save is not called
    }
}
