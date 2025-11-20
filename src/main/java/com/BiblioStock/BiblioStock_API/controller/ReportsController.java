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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.BiblioStock.BiblioStock_API.dto.ProductsPerCategoryDTO;
import com.BiblioStock.BiblioStock_API.service.ProductService;
import com.BiblioStock.BiblioStock_API.dto.BalanceRequestDTO;
import com.BiblioStock.BiblioStock_API.dto.BalanceResponseDTO;
import com.BiblioStock.BiblioStock_API.dto.ProductResponseDTO;
import com.BiblioStock.BiblioStock_API.service.ReportsService;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
@Tag(name = "Reports", description = "Operações relacionadas a relatórios de produtos")
public class ReportsController {

    private final ProductService productService;
    private final ReportsService reportsService;


    public ReportsController(ProductService service, ReportsService reportsService) {
        this.productService = service;
        this.reportsService = reportsService;

    }

    @Operation(summary = "Lista produtos por categoria", description = "Retorna a quantidade de produtos em cada categoria.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relatório retornado com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProductsPerCategoryDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/products-per-category")
    public ResponseEntity<List<ProductsPerCategoryDTO>> getProductsPerCategory() {
        return ResponseEntity.ok(productService.getProductsPerCategory());
    }

    @Operation(summary = "Lista produtos de uma categoria específica", description = "Retorna a quantidade de produtos de uma categoria específica pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relatório retornado com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProductsPerCategoryDTO.class))),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content),
        @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/products-per-category/{categoryId}")
    public ResponseEntity<List<ProductsPerCategoryDTO>> getProductsPerCategory(
        @Parameter(description = "ID da categoria", required = true)
        @PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsPerCategoryByCategoryId(categoryId));
    }
    
    @Operation(summary = "Relatório de Balanço de Estoque (RF025)", description = "Retorna todos os produtos com quantidade, preço unitário e valor total de estoque.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Relatório retornado com sucesso",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = BalanceRequestDTO.class))),
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/balance")
        public ResponseEntity<BalanceResponseDTO> getBalanceReport() {
        List<BalanceRequestDTO> balance = reportsService.getBalance();
        BigDecimal totalInventoryValue = reportsService.getTotalInventoryValue();
        
        BalanceResponseDTO response = new BalanceResponseDTO(balance, totalInventoryValue);
        return ResponseEntity.ok(response);
    }
     @Operation(
        summary = "Relatório de Produtos por Autor (RF0XX)",
        description = "Retorna todos os produtos cadastrados que pertencem a um autor específico, identificado pelo ID do autor."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Relatório de produtos retornado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Autor não encontrado", content = @Content),
        @ApiResponse(responseCode ="500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/products-per-author/{authorId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsPerAuthor(
            @Parameter(description = "ID do autor", example = "1", required = true)
            @PathVariable Long authorId) {

        List<ProductResponseDTO> list = reportsService.getProductsPerAuthor(authorId);
        return ResponseEntity.ok(list);
    }

}
