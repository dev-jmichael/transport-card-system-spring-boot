package com.project.TransportCardSystem.application.transportcard.dto;

import jakarta.validation.constraints.NotEmpty;

public record CreateTransportCardRequest(
        @NotEmpty(message = "Card type cannot be empty.")
        String cardType
) { }
