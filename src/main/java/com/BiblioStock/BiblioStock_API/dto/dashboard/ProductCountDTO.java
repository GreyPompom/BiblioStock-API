package com.BiblioStock.BiblioStock_API.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProductCountDTO", description = "Total de produtos cadastrados")
public record ProductCountDTO(
        @Schema(description = "Total de produtos cadastrados", example = "42")
        long totalProducts, 
        @Schema(description = "Total de categorias cadastradas", example = "10")
        long totalCategories
        ) {

}
