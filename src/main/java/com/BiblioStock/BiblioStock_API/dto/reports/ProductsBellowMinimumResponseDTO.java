package com.BiblioStock.BiblioStock_API.dto.reports;

import java.math.BigDecimal;

import com.BiblioStock.BiblioStock_API.model.Author;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProductsBellowMinimumResponseDTO", description = "DTO de resposta com os dados de relatorio")
public record ProductsBellowMinimumResponseDTO(
        @Schema(description = "ID do product", example = "1", required = true)
        Long productId,
        @Schema(description = "Nome do product", example = "É assim que acaba", required = true)
        String productName,
        @Schema(description = "Categoria do product", example = "Romance", required = true)
        String categoryName,
        @Schema(description = "Quantidade mínima", example = "1", required = true)
        BigDecimal minQTD,
        @Schema(description = "Quantidade do estoque", example = "12", required = true)
        BigDecimal stockQTD,
        @Schema(description = "Quantidade em falta", example = "3", required = true)
        BigDecimal deficit
        ) {

}
