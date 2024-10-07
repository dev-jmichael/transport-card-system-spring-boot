package com.project.TransportCardSystem.application.transportcard;

import com.project.TransportCardSystem.application.transportcard.dto.CreateTransportCardRequest;
import com.project.TransportCardSystem.application.transportcard.mapper.TransportCardMapper;
import com.project.TransportCardSystem.api.common.exceptions.CardNotFoundException;
import com.project.TransportCardSystem.application.transportcard.dto.TransportCardResponse;
import com.project.TransportCardSystem.api.common.constants.TransportCardType;
import com.project.TransportCardSystem.domain.entities.TransportCard;
import com.project.TransportCardSystem.infrastructure.repositories.TransportCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportCardService {
    private static final Logger logger = LoggerFactory.getLogger(TransportCardService.class);

    private final TransportCardRepository repository;

    private final TransportCardMapper mapper;

    public TransportCardService(TransportCardRepository repository, TransportCardMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public TransportCardResponse createTransportCard(CreateTransportCardRequest request) {
        logger.info("Creating transport card for card type: {}", request.cardType());
        var transportCardType = TransportCardType.getTransportCardType(request.cardType().toUpperCase());
        var transportCard = TransportCard.builder()
                .loadAmount(transportCardType.getInitialLoadAmount())
                .yearsOfValidity(transportCardType.getYearsOfValidity())
                .cardType(transportCardType)
                .build();
        var savedCard = repository.save(transportCard);
        logger.info("Successfully created transport card with transportCard number: {}", savedCard.getCardNumber());
        return mapper.toDTO(savedCard);
    }

    public List<TransportCardResponse> getAllTransportCards() {
        logger.info("Fetching all transport cards...");
        var transportCards = repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
        logger.info("Successfully retrieved {} transport cards", transportCards.size());
        return transportCards;
    }

    public TransportCardResponse getTransportCardById(Long cardNumber) {
        logger.info("Fetching transport card with transportCard number: {}", cardNumber);
        var transportCard = repository.findById(cardNumber)
                .map(mapper::toDTO)
                .orElseThrow(() -> new CardNotFoundException(cardNumber));
        logger.info("Transport card found with card number: {}", cardNumber);
        return transportCard;
    }
}
