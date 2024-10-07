package com.project.TransportCardSystem.application.cardtransaction;

import com.project.TransportCardSystem.api.common.exceptions.CardNotFoundException;
import com.project.TransportCardSystem.api.common.exceptions.InsufficientBalanceException;
import com.project.TransportCardSystem.api.common.constants.TransportCardType;
import com.project.TransportCardSystem.domain.entities.TransportCard;
import com.project.TransportCardSystem.infrastructure.repositories.TransportCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CardTransactionService {
    private static final Logger logger = LoggerFactory.getLogger(CardTransactionService.class);

    private final TransportCardRepository repository;

    public CardTransactionService(TransportCardRepository repository) {
        this.repository = repository;
    }

    public String processFareDeduction(Long cardNumber) {
        logger.info("Processing fare deduction for card number: {}", cardNumber);

        var transportCard = repository.findById(cardNumber)
                .orElseThrow(() -> new CardNotFoundException(cardNumber));

        logger.info("Transport card found for card number: {}", cardNumber);

        var cardType = TransportCardType.getTransportCardType(transportCard.getCardType().name());
        var fare = cardType.calculateFare();
        logger.info("Fare calculated for card type {}: {}", cardType, fare);

        if (transportCard.getLoadAmount().compareTo(fare) < 0) {
            logger.warn("Insufficient balance on card number {}. Available balance: {}, Required fare: {}",
                    cardNumber, transportCard.getLoadAmount(), fare);
            throw new InsufficientBalanceException("Insufficient balance on the card.");
        }

        transportCard.setLoadAmount(transportCard.getLoadAmount().subtract(fare));
        var updatedTransportCard = repository.save(transportCard);
        logger.info("Fare deducted successfully. Updated balance for card number {}: {}", cardNumber, updatedTransportCard.getLoadAmount());

        return "Current balance: " + updatedTransportCard.getLoadAmount();
    }
}
