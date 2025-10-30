package com.BiblioStock.BiblioStock_API.dto;

import com.BiblioStock.BiblioStock_API.model.Category;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CategoryResponseDTO", description = "DTO de resposta com os dados de uma categoria")
public record CategoryResponseDTO(
    
        @Schema(description = "ID da categoria", example = "1", required = true)
        Long id,

        @Schema(description = "Nome da categoria", example = "Livros", required = true)
        String name,

        @Schema(description = "Tamanho da categoria", example = "Médio", required = true)
        String size,

        @Schema(description = "Tipo de embalagem da categoria", example = "Caixa", required = true)
        String packagingType,

        @Schema(description = "Percentual de reajuste padrão da categoria", example = "10.5", required = true)
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
