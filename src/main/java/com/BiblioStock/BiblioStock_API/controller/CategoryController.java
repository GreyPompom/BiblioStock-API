package com.BiblioStock.BiblioStock_API.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.BiblioStock.BiblioStock_API.dto.*;
import com.BiblioStock.BiblioStock_API.service.CategoryService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
@Tag(name = "Category", description = "Operações relacionadas a categorias")
public class CategoryController {

    private final CategoryService service;
    
    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @Operation(summary = "Lista todas as categorias", description = "Retorna uma lista com todas as categorias cadastradas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Busca categoria por ID", description = "Retorna os dados de uma categoria específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria encontrada",
            content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getById(
        @Parameter(description = "ID da categoria") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Cria uma nova categoria", description = "Adiciona uma nova categoria ao sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso",
            content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(
        @Parameter(description = "DTO com os dados da categoria", required = true)
        @Valid @RequestBody CategoryRequestDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(summary = "Atualiza uma categoria", description = "Atualiza os dados de uma categoria existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada", content = @Content),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(
        @Parameter(description = "ID da categoria", required = true) 
        @PathVariable Long id,
        @Parameter(description = "DTO com os dados atualizados", required = true) 
        @Valid @RequestBody CategoryRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Exclui uma categoria", description = "Remove uma categoria caso não esteja associada a produtos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso", content = @Content),
        @ApiResponse(responseCode = "400", description = "Não é permitido excluir categoria com vínculo", content = @Content),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @Parameter(description = "ID da categoria", required = true) 
        @PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok("Categoria excluída com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest()
                    .body("Não é permitido excluir a categoria pois há produtos vinculados a ela.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado ao excluir categoria: " + e.getMessage());
        }
    }

}
