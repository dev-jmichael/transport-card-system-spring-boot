package com.project.TransportCardSystem.application;

import com.project.TransportCardSystem.application.transportcard.TransportCardService;
import com.project.TransportCardSystem.application.transportcard.dto.CreateTransportCardRequest;
import com.project.TransportCardSystem.application.transportcard.dto.TransportCardResponse;
import com.project.TransportCardSystem.application.transportcard.mapper.TransportCardMapper;
import com.project.TransportCardSystem.api.common.constants.TransportCardType;
import com.project.TransportCardSystem.api.common.exceptions.CardNotFoundException;
import com.project.TransportCardSystem.api.common.exceptions.InvalidCardTypeException;
import com.project.TransportCardSystem.domain.entities.TransportCard;
import com.project.TransportCardSystem.infrastructure.repositories.TransportCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransportCardServiceTest {
    @Mock
    private TransportCardRepository repository;

    @Mock
    private TransportCardMapper mapper;

    @InjectMocks
    private TransportCardService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

    @Test
    public void testCreateRegularTransportCard_Success() {
        // Arrange
        CreateTransportCardRequest request = new CreateTransportCardRequest("Regular");

        TransportCardType type = TransportCardType.REGULAR;
        TransportCard card = TransportCard.builder()
                .loadAmount(type.getInitialLoadAmount())
                .yearsOfValidity(type.getYearsOfValidity())
                .cardType(type)
                .build();

        TransportCardResponse response = new TransportCardResponse(
                card.getCardNumber(),
                type.getInitialLoadAmount(),
                type.getYearsOfValidity(),
                type
        );

        when(repository.save(any(TransportCard.class))).thenReturn(card);
        when(mapper.toDTO(card)).thenReturn(response);

        // Act
        TransportCardResponse result = service.createTransportCard(request);

        // Assert
        assertNotNull(result);  // Check if the result is not null
        verify(repository, times(1)).save(any(TransportCard.class));  // Ensure repository's save is called
        verify(mapper, times(1)).toDTO(card);  // Ensure mapping to DTO occurs
    }

    @Test
    public void testCreateDiscountedTransportCard_Success() {
        // Arrange
        CreateTransportCardRequest request = new CreateTransportCardRequest("Discounted");

        TransportCardType type = TransportCardType.DISCOUNTED;
        TransportCard card = TransportCard.builder()
                .loadAmount(type.getInitialLoadAmount())
                .yearsOfValidity(type.getYearsOfValidity())
                .cardType(type)
                .build();

        TransportCardResponse response = new TransportCardResponse(
                card.getCardNumber(),
                type.getInitialLoadAmount(),
                type.getYearsOfValidity(),
                type
        );

        when(repository.save(any(TransportCard.class))).thenReturn(card);
        when(mapper.toDTO(card)).thenReturn(response);

        // Act
        TransportCardResponse result = service.createTransportCard(request);

        // Assert
        assertNotNull(result);  // Check if the result is not null
        verify(repository, times(1)).save(any(TransportCard.class));  // Ensure repository's save is called
        verify(mapper, times(1)).toDTO(card);  // Ensure mapping to DTO occurs
    }

    @Test
    public void testCreateTransportCard_InvalidCardType() {
        // Arrange
        CreateTransportCardRequest request = new CreateTransportCardRequest("INVALID");

        // Act & Assert
        InvalidCardTypeException exception = assertThrows(InvalidCardTypeException.class, () -> {
            service.createTransportCard(request);
        });

        assertEquals("Invalid card type: 'INVALID'.", exception.getMessage());  // Ensure the correct error message
        verify(repository, never()).save(any());  // Ensure repository.save is never called
    }

    @Test
    public void testGetAllTransportCards_Success() {
        // Arrange
        TransportCard card1 = TransportCard.builder()
                .cardNumber(1L)
                .loadAmount(BigDecimal.valueOf(100.00))
                .yearsOfValidity(5)
                .cardType(TransportCardType.REGULAR)
                .build();

        TransportCard card2 = TransportCard.builder()
                .cardNumber(2L)
                .loadAmount(BigDecimal.valueOf(500.00))
                .yearsOfValidity(3)
                .cardType(TransportCardType.DISCOUNTED)
                .build();

        List<TransportCard> cards = Arrays.asList(card1, card2);

        TransportCardResponse response1 = new TransportCardResponse(
                1L,
                BigDecimal.valueOf(100.00),
                5,
                TransportCardType.REGULAR
        );

        TransportCardResponse response2 = new TransportCardResponse(
                2L,
                BigDecimal.valueOf(500.00),
                3,
                TransportCardType.DISCOUNTED
        );

        when(repository.findAll()).thenReturn(cards);
        when(mapper.toDTO(card1)).thenReturn(response1);
        when(mapper.toDTO(card2)).thenReturn(response2);

        // Act
        List<TransportCardResponse> result = service.getAllTransportCards();

        // Assert
        assertEquals(2, result.size());  // Ensure that two responses are returned
        verify(repository, times(1)).findAll();  // Ensure findAll is called once

        // Use ArgumentCaptor to capture the exact calls to mapper.toDTO()
        ArgumentCaptor<TransportCard> captor = ArgumentCaptor.forClass(TransportCard.class);
        verify(mapper, times(2)).toDTO(captor.capture());  // Ensure toDTO is called twice

        List<TransportCard> capturedCards = captor.getAllValues();
        assertEquals(card1, capturedCards.get(0));  // Verify the first call was for card1
        assertEquals(card2, capturedCards.get(1));  // Verify the second call was for card2
    }

    @Test
    public void testGetTransportCardById_Success() {
        // Arrange
        Long cardNumber = 123L;
        TransportCard card = TransportCard.builder()
                .cardNumber(cardNumber)
                .loadAmount(BigDecimal.valueOf(100.00))
                .yearsOfValidity(5)
                .cardType(TransportCardType.REGULAR)
                .build();

        TransportCardResponse response = new TransportCardResponse(
                cardNumber,
                BigDecimal.valueOf(100.00),
                5,
                TransportCardType.REGULAR
        );

        when(repository.findById(cardNumber)).thenReturn(Optional.of(card));
        when(mapper.toDTO(card)).thenReturn(response);

        // Act
        TransportCardResponse result = service.getTransportCardById(cardNumber);

        // Assert
        assertNotNull(result);  // Ensure the card is found and not null
        verify(repository, times(1)).findById(cardNumber);  // Ensure repository's findById is called
        verify(mapper, times(1)).toDTO(card);  // Ensure mapper is called
    }

    @Test
    public void testGetTransportCardById_NotFound() {
        // Arrange
        Long cardNumber = 123L;

        when(repository.findById(cardNumber)).thenReturn(Optional.empty());  // Simulate card not found

        // Act & Assert
        assertThrows(CardNotFoundException.class, () -> {
            service.getTransportCardById(cardNumber);
        });

        verify(repository, times(1)).findById(cardNumber);  // Ensure findById is called once
        verify(mapper, never()).toDTO(any());  // Ensure mapper is never called when card not found
    }
}
