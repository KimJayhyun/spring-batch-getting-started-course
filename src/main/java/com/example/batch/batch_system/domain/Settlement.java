package com.example.batch.batch_system.domain;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private String storeName;
    private Integer settlementAmount;
    private LocalDate settlementDate;

    public Settlement(Long orderId, String storeName, Integer settlementAmount,
            LocalDate settlementDate) {
        this.orderId = orderId;
        this.storeName = storeName;
        this.settlementAmount = settlementAmount;
        this.settlementDate = settlementDate;
    }
}
