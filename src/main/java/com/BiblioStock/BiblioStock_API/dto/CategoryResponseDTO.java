package com.BiblioStock.BiblioStock_API.dto;

import com.BiblioStock.BiblioStock_API.model.Category;
import java.math.BigDecimal;
public record CategoryResponseDTO(
        Long id,
        String name,
        String size,
        String packagingType,
        BigDecimal defaultAdjustmentPercent
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

    public Category toEntity() {
        Category category = new Category();
        category.setId(this.id);
        category.setName(this.name);
        category.setSize(this.size);
        category.setPackagingType(this.packagingType);
        category.setDefaultAdjustmentPercent(this.defaultAdjustmentPercent);
        return category;
    }
}
