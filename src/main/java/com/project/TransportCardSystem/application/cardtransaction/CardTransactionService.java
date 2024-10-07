package com.project.TransportCardSystem.application.cardtransaction;

import com.project.TransportCardSystem.api.common.exceptions.CardNotFoundException;
import com.project.TransportCardSystem.api.common.exceptions.InsufficientBalanceException;
import com.project.TransportCardSystem.api.common.constants.TransportCardType;
import com.project.TransportCardSystem.domain.entities.TransportCard;
import com.project.TransportCardSystem.infrastructure.repositories.TransportCardRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CardTransactionService {
    private final TransportCardRepository repository;

    public CardTransactionService(TransportCardRepository repository) {
        this.repository = repository;
    }

    public String processFareDeduction(Long cardNumber) {
        var transportCard = repository.findById(cardNumber)
                .orElseThrow(() -> new CardNotFoundException(cardNumber));
        var cardType = TransportCardType.getTransportCardType(transportCard.getCardType().name());
        var fare = cardType.calculateFare();

        if (transportCard.getLoadAmount().compareTo(fare) < 0) {
            throw new InsufficientBalanceException("Insufficient balance on the card.");
        }

        transportCard.setLoadAmount(transportCard.getLoadAmount().subtract(fare));
        var updatedTransportCard = repository.save(transportCard);

        return "Current balance: " + updatedTransportCard.getLoadAmount();
    }
}
