package com.project.TransportCardSystem.api.common.exceptions;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(Long cardNumber) {
        super("Card not found with card number: " + cardNumber);
    }
}
