package com.project.TransportCardSystem.api;

import com.project.TransportCardSystem.application.cardtransaction.CardTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CardTransactionController {
    private final CardTransactionService service;
    public CardTransactionController(CardTransactionService service) {
        this.service = service;
    }
    @Operation(tags = "Card Transactions", summary = "Process fare deduction.", description = "Processes fare deduction via commuter's card number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "402", description = "Insufficient balance exception.")
    })
    @PatchMapping("/transport-cards/{cardNumber}")
    public ResponseEntity<String> deductFare(@PathVariable Long cardNumber) {
        return ResponseEntity.ok(service.processFareDeduction(cardNumber));
    }
}
