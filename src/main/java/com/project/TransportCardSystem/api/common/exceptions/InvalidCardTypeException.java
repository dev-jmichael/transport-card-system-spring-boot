package com.project.TransportCardSystem.api.common.exceptions;

public class InvalidCardTypeException extends RuntimeException {
    public InvalidCardTypeException(String message) {
        super(message);
    }
}
