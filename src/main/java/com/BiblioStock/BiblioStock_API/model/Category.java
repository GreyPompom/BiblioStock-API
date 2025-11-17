package com.BiblioStock.BiblioStock_API.model;

import java.time.OffsetDateTime;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Table(name = "categories")
@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, length = 20)
    private String size; // Pequeno, Médio, Grande

    @Column(name = "packaging_type",nullable = false)
    private String packagingType;

    @Column(name = "default_adjustment_percent", nullable = false)
    private BigDecimal defaultAdjustmentPercent = BigDecimal.ZERO;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

}
