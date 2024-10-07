package com.project.TransportCardSystem.application.transportcard;

import com.project.TransportCardSystem.application.transportcard.dto.CreateTransportCardRequest;
import com.project.TransportCardSystem.application.transportcard.mapper.TransportCardMapper;
import com.project.TransportCardSystem.api.common.exceptions.CardNotFoundException;
import com.project.TransportCardSystem.application.transportcard.dto.TransportCardResponse;
import com.project.TransportCardSystem.api.common.constants.TransportCardType;
import com.project.TransportCardSystem.domain.entities.TransportCard;
import com.project.TransportCardSystem.infrastructure.repositories.TransportCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportCardService {
    private final TransportCardRepository repository;

    private final TransportCardMapper mapper;

    public TransportCardService(TransportCardRepository repository, TransportCardMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public TransportCardResponse createTransportCard(CreateTransportCardRequest request) {
        var type = TransportCardType.getTransportCardType(request.cardType().toUpperCase());
        var card = TransportCard.builder()
                .loadAmount(type.getInitialLoadAmount())
                .yearsOfValidity(type.getYearsOfValidity())
                .cardType(type)
                .build();
        return mapper.toDTO(repository.save(card));
    }

    public List<TransportCardResponse> getAllTransportCards() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public TransportCardResponse getTransportCardById(Long cardNumber) {
        return repository.findById(cardNumber)
                .map(mapper::toDTO)
                .orElseThrow(() -> new CardNotFoundException(cardNumber));
    }
}
