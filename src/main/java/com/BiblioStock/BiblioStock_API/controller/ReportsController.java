package com.BiblioStock.BiblioStock_API.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.BiblioStock.BiblioStock_API.dto.ProductsPerCategoryDTO;
import com.BiblioStock.BiblioStock_API.dto.reports.BalanceRequestDTO;
import com.BiblioStock.BiblioStock_API.dto.reports.BalanceResponseDTO;
import com.BiblioStock.BiblioStock_API.dto.reports.MovementsHistoryReportDTO;
import com.BiblioStock.BiblioStock_API.dto.reports.ProductPricesDTO;
import com.BiblioStock.BiblioStock_API.dto.reports.ProductsBellowMinimumResponseDTO;
import com.BiblioStock.BiblioStock_API.service.ProductService;
import com.BiblioStock.BiblioStock_API.service.ReportsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
@Tag(name = "Reports", description = "Operações relacionadas a relatórios de produtos")
public class ReportsController {

    private final ProductService productService;
    private final ReportsService reportsService;

    public ReportsController(ProductService productService, ReportsService reportsService) {
        this.productService = productService;
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

        BalanceResponseDTO response = new BalanceResponseDTO(balance, totalInventoryValue, BigDecimal.ZERO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Produtos Abaixo da Quantidade Mínima (RF026)", description = "Exibe ID, nome, quantidade mínima e quantidade atual.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relatório retornado com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = BalanceRequestDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/products-below-minimum")
    public List<ProductsBellowMinimumResponseDTO> getProductsBellowMinimum() {
        return reportsService.getProductsBellowMinimum();
    }

    @Operation(summary = "Resumo de Vendas", description = "Retorna um resumo das vendas entre as datas especificadas.")
    @GetMapping("/sales-summary")
    public ResponseEntity<?> getSalesSummary(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return reportsService.getSalesReport(startDate, endDate)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Operation(summary = "Relatório de preços de produtos",
            description = "Retorna lista de produtos com preço unitário e preço com reajuste.")
    @GetMapping("/products-prices")
    public List<ProductPricesDTO> getProductPricesReport() {
        return reportsService.getProductPricesReport();
    }

    @Operation(summary = "Relatório de histórico de movimentações",
            description = "Retorna o histórico de movimentações por produto, incluindo entradas, saídas, saldo e produtos mais/menos vendidos.")
    @GetMapping("/movements-history")
    public MovementsHistoryReportDTO getMovementsHistoryReport() {
        return reportsService.getMovementsHistoryReport();
    }

}
