package com.BiblioStock.BiblioStock_API.dto;

import java.math.BigDecimal;

import com.BiblioStock.BiblioStock_API.model.PriceAdjustment;

public record PriceAdjustmentResponseDTO(
        String scopeType,
        BigDecimal percent,
        Long categoryId,
        String nameCategory,
        String note
) {
    public static PriceAdjustmentResponseDTO fromEntity(PriceAdjustment entity) {
        return new PriceAdjustmentResponseDTO(
                entity.getScopeType(),
                entity.getPercent(),
                entity.getCategory() != null ? entity.getCategory().getId() : null,
                entity.getCategory() != null ? entity.getCategory().getName() : null,
                entity.getNote()
        );
    }
}
