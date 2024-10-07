package com.project.TransportCardSystem.application.transportcard.dto;

import com.project.TransportCardSystem.api.common.constants.TransportCardType;

import java.math.BigDecimal;

public record TransportCardResponse(
        Long cardNumber,
        BigDecimal loadAmount,
        Integer yearsOfValidity,
        TransportCardType cardType
) {

}
