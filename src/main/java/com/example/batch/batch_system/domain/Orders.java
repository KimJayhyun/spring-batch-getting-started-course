package com.example.batch.batch_system.domain;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String storeName;
    private Integer amount;
    private LocalDate orderDate; // yyyy-MM-dd

    public Orders(Long id, String customerName, String storeName, Integer amount,
            LocalDate orderDate) {
        this.id = id;
        this.customerName = customerName;
        this.storeName = storeName;
        this.amount = amount;
        this.orderDate = orderDate;
    }

}
