package com.project.TransportCardSystem.domain.entities;

import com.project.TransportCardSystem.api.common.constants.TransportCardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransportCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long cardNumber;

    @Column
    BigDecimal loadAmount;

    @Column
    Integer yearsOfValidity;

    @Enumerated(value = EnumType.STRING)
    TransportCardType cardType;
}
