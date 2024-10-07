package com.project.TransportCardSystem.api;

import com.project.TransportCardSystem.api.common.dto.RestApiResponse;
import com.project.TransportCardSystem.application.transportcard.dto.CreateTransportCardRequest;
import com.project.TransportCardSystem.application.transportcard.TransportCardService;
import com.project.TransportCardSystem.application.transportcard.dto.TransportCardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TransportCardController {
    private final TransportCardService transportCardService;
    public TransportCardController(TransportCardService transportCardService) {
        this.transportCardService = transportCardService;
    }
    @Operation(tags = "Transport Card", summary = "Create a new transport card", description = "Creates a new transport card and returns the card details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Validation issues",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "InvalidCardType", value = "{\"message\": \"Invalid card type exception.\"}"),
                                    @ExampleObject(name = "EmptyCardType", value = "{\"message\": \"Card type cannot be empty.\"}")
                            }
                    )
            )
    })
    @PostMapping("/transport-cards")
    public ResponseEntity<RestApiResponse<TransportCardResponse>> createTransportCard(
            @Parameter(description = "Card creation request", required = true)
            @RequestBody @Valid CreateTransportCardRequest request
    ) {
        var response = transportCardService.createTransportCard(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RestApiResponse<>(true, response));
    }
    @Operation(tags = "Transport Card", summary = "Retrieve all transport cards", description = "Retrieves all transport cards with its details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping("/transport-cards")
    public ResponseEntity<RestApiResponse<List<TransportCardResponse>>> getAllTransportCards() {
        return ResponseEntity.ok(
                new RestApiResponse<>(true, transportCardService.getAllTransportCards())
        );
    }
    @Operation(tags = "Transport Card", summary = "Retrieve transport card by card number.", description = "Retrieves specific card details by card number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Card not found exception.")
    })
    @GetMapping("/transport-cards/{cardNumber}")
    public ResponseEntity<RestApiResponse<TransportCardResponse>> getTransportCardById(@PathVariable Long cardNumber) {
        return ResponseEntity.ok(
                new RestApiResponse<>(true, transportCardService.getTransportCardById(cardNumber))
        );
    }
}
