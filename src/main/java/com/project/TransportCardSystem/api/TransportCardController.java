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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TransportCardController {
    private static final Logger logger = LoggerFactory.getLogger(TransportCardController.class);

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
        logger.info("Received request to create transport card for type: {}", request.cardType());
        var response = transportCardService.createTransportCard(request);
        logger.info("Successfully created transport card with card number: {}", response.cardNumber());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RestApiResponse<>(true, response));
    }

    @Operation(tags = "Transport Card", summary = "Retrieve all transport cards", description = "Retrieves all transport cards with its details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping("/transport-cards")
    public ResponseEntity<RestApiResponse<List<TransportCardResponse>>> getAllTransportCards() {
        logger.info("Received request to retrieve all transport cards");
        var response = transportCardService.getAllTransportCards();
        logger.info("Successfully retrieved {} transport cards", response.size());
        return ResponseEntity.ok(
                new RestApiResponse<>(true, response)
        );
    }

    @Operation(tags = "Transport Card", summary = "Retrieve transport card by card number.", description = "Retrieves specific card details by card number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Card not found exception.")
    })
    @GetMapping("/transport-cards/{cardNumber}")
    public ResponseEntity<RestApiResponse<TransportCardResponse>> getTransportCardById(@PathVariable Long cardNumber) {
        logger.info("Received request to retrieve transport card with card number: {}", cardNumber);
        var response = transportCardService.getTransportCardById(cardNumber);
        logger.info("Successfully retrieved transport card with card number: {}", cardNumber);
        return ResponseEntity.ok(
                new RestApiResponse<>(true, response)
        );
    }
}
