package com.project.TransportCardSystem.api.common.constants;

import com.project.TransportCardSystem.api.common.exceptions.InvalidCardTypeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum TransportCardType {
    REGULAR(BigDecimal.valueOf(100.00), 5, BigDecimal.valueOf(15.00)) {
        @Override
        public BigDecimal calculateFare() {
            return getFareRate();
        }
    },

    DISCOUNTED(BigDecimal.valueOf(500.00), 3, BigDecimal.valueOf(10.00)) {
        private final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.20);

        @Override
        public BigDecimal calculateFare() {
            return getFareRate().multiply(BigDecimal.ONE.subtract(DISCOUNT_RATE));
        }
    };

    private final BigDecimal initialLoadAmount;
    private final Integer yearsOfValidity;
    private final BigDecimal fareRate;

    public abstract BigDecimal calculateFare();

    private static final Map<String, TransportCardType> CARD_TYPE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(type -> type.name().toLowerCase(), type -> type));
    public static TransportCardType getTransportCardType(String cardType) {
        return Optional.ofNullable(CARD_TYPE_MAP.get(cardType.toLowerCase()))
                .orElseThrow(() -> new InvalidCardTypeException("Invalid card type: '" + cardType + "'."));
    }
}
