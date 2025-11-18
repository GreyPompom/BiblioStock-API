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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.BiblioStock.BiblioStock_API.dto.ProductRequestDTO;
import com.BiblioStock.BiblioStock_API.dto.ProductResponseDTO;
import com.BiblioStock.BiblioStock_API.service.ProductService;
import jakarta.validation.Valid;
import com.BiblioStock.BiblioStock_API.service.ProductAuthorReportService;


@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@Tag(name = "Products", description = "Operações relacionadas a produtos")
public class ProductController {

    private final ProductService service;
    private final ProductAuthorReportService productAuthorReportService;


    public ProductController(ProductService service,
                         ProductAuthorReportService productAuthorReportService) {
    this.service = service;
    this.productAuthorReportService = productAuthorReportService;
}

    @Operation(summary = "Lista todos os produtos", description = "Retorna uma lista de todos os produtos cadastrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProductResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Busca produto por ID", description = "Retorna os dados de um produto específico pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto encontrado",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProductResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(
        @Parameter(description = "ID do produto", required = true)
        @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Cria um novo produto", description = "Adiciona um novo produto ao sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProductResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
        @ApiResponse(responseCode = "409", description = "Conflito - Produto já existe", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(
        @Parameter(description = "DTO com os dados do produto", required = true)
        @Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }

    @Operation(summary = "Atualiza um produto existente", description = "Atualiza os dados de um produto pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProductResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou violação de regra de negócio", content = @Content),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(
        @Parameter(description = "ID do produto", required = true)
        @PathVariable Long id,
        @Parameter(description = "DTO com os dados atualizados", required = true)
        @Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Deleta um produto", description = "Remove um produto do sistema pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso", content = @Content),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "Não é permitido excluir produto com vínculos", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @Parameter(description = "ID do produto", required = true)
        @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lista produtos por categoria", description = "Retorna todos os produtos pertencentes a uma categoria específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProductResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("by-category/{id}")
    public ResponseEntity<List<ProductResponseDTO>> getByCategoryId(
         @Parameter(description = "ID da categoria", required = true)
         @PathVariable Long id) {
        return ResponseEntity.ok(service.findByCategory(id));
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
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/reports/products-per-author/{authorId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsPerAuthor(
            @Parameter(description = "ID do autor", example = "1", required = true)
            @PathVariable Long authorId) {

        List<ProductResponseDTO> list = productAuthorReportService.getProductsPerAuthor(authorId);
        return ResponseEntity.ok(list);
    }


    
}
