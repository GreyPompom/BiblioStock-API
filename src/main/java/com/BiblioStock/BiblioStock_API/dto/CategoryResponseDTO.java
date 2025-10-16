package com.BiblioStock.BiblioStock_API.dto;

import com.BiblioStock.BiblioStock_API.model.Category;

public record CategoryResponseDTO(
        Long id,
        String name,
        String size,
        String packagingType,
        Double defaultAdjustmentPercent
) {
    public static CategoryResponseDTO fromEntity(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getSize(),
                category.getPackagingType(),
                category.getDefaultAdjustmentPercent()
        );
    }
}
