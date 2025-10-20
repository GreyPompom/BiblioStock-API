package com.BiblioStock.BiblioStock_API.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "price_adjustments")
public class PriceAdjustment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String scopeType; // GLOBAL ou CATEGORIA
    private BigDecimal percent;
    private String note;

    @ManyToOne
    @JoinColumn(name = "applied_by")
    private User appliedBy;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private OffsetDateTime appliedAt = OffsetDateTime.now();

}
