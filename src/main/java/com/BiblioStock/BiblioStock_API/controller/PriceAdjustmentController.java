package com.BiblioStock.BiblioStock_API.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.BiblioStock.BiblioStock_API.dto.CategoryResponseDTO;
import com.BiblioStock.BiblioStock_API.dto.PriceAdjustmentRequestDTO;
import com.BiblioStock.BiblioStock_API.dto.PriceAdjustmentResponseDTO;
import com.BiblioStock.BiblioStock_API.model.PriceAdjustment;
import com.BiblioStock.BiblioStock_API.service.PriceAdjustmentService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/prices")
@Tag(name = "Price Adjustments", description = "Operações relacionadas a reajustes de preço")
public class PriceAdjustmentController {

    private final PriceAdjustmentService service;

    @Autowired
    public PriceAdjustmentController(PriceAdjustmentService service) {
        this.service = service;
    }

    @Operation(summary = "Aplica um reajuste de preço", description = "Aplica um novo reajuste nos produtos de acordo com os dados informados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reajuste aplicado com sucesso", content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada", content = @Content),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
        @ApiResponse(responseCode = "409", description = "Conflito - Reajuste já existente ou conflito de valores", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor ao aplicar o reajuste", content = @Content)
    })
    @PostMapping("/adjust")
    public ResponseEntity<String> applyAdjustment(
        @Parameter(description = "DTO com os dados do reajuste", required = true)
        @Valid @RequestBody PriceAdjustmentRequestDTO dto) {
        service.applyAdjustment(dto);
        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Lista histórico de reajustes de preço", description = "Retorna uma lista com todos os reajustes aplicados no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceAdjustment.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor ao buscar histórico", content = @Content)
    })
    @GetMapping("/history")
    public ResponseEntity<List<PriceAdjustment>> getHistory() {
        return ResponseEntity.ok(service.listHistory());
    }
    
    //Retornar percentuais de ajuste de preço por categoria
    @GetMapping("/category-percent")
    public ResponseEntity<List<PriceAdjustmentResponseDTO>> getCategoriesPercent() {
        return ResponseEntity.ok(service.getAllCategoriesPercent());
    }

    @GetMapping("/category-percent/{categoryId}")
    public ResponseEntity<PriceAdjustmentResponseDTO> getCategoryPercent(@PathVariable Long categoryId) {
        return ResponseEntity.ok(service.getCategoryPercent(categoryId));
    }
}
