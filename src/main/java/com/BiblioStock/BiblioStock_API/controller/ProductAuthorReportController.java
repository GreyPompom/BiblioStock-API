package com.BiblioStock.BiblioStock_API.controller;

import com.BiblioStock.BiblioStock_API.dto.ProductResponseDTO;
import com.BiblioStock.BiblioStock_API.service.ProductAuthorReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ProductAuthorReportController {
    private final ProductAuthorReportService productAuthorReportService;

    public ProductAuthorReportController(ProductAuthorReportService productAuthorReportService) {
        this.productAuthorReportService = productAuthorReportService;
    }

    @Operation(summary = "Relatório de Produtos por Autor (RF0XX)", description = "Retorna todos os produtos cadastrados que pertencem a um autor específico, identificado pelo ID do autor.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "Relatório de produtos retornado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "500",
            description = "Erro interno no servidor",
            content = @Content
        )
    })
    @GetMapping("/products-per-author/{authorId}")
    public List<ProductResponseDTO> getProductsPerAuthor(@Parameter(description = "ID do autor", example = "1", required = true)
            @PathVariable Long authorId) {

        return productAuthorReportService.getProductsPerAuthor(authorId);
    }
}
    

