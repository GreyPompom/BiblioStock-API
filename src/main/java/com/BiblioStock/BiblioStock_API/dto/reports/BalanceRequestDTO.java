package com.BiblioStock.BiblioStock_API.dto.reports;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name= "BalanceRequestDTO", description = "Item individual do balanço (RF025)")
public record BalanceRequestDTO(

        @Schema(description = "ID do produto", example = "1")
        Long id,

        @Schema(description = "Nome do produto", example = "Caneta Azul 1mm")
        String name,

        @Schema(description = "Quantidade em estoque", example = "150")
        Integer stockQty,

        @Schema(description = "Preço unitário do produto", example = "2.50")
        BigDecimal price,

        @Schema(description = "Valor total do produto em estoque (quantidade x preço)", example = "375.00")
        BigDecimal totalValue

) {}
