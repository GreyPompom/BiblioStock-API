package com.BiblioStock.BiblioStock_API.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import com.BiblioStock.BiblioStock_API.dto.AuthorRequestDTO;
import com.BiblioStock.BiblioStock_API.dto.AuthorResponseDTO;
import com.BiblioStock.BiblioStock_API.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@CrossOrigin(origins = "*")
@Tag(name = "Authors", description = "Operações relacionadas a autores")
public class AuthorController {

    private final AuthorService service;

    public AuthorController(AuthorService service) {
        this.service = service;
    }

    @Operation(summary = "Lista todos os autores", description = "Retorna uma lista de todos os autores cadastrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = AuthorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<AuthorResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }


    @Operation(summary = "Busca autor por ID", description = "Retorna os dados de um autor específico pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autor encontrado",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = AuthorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Autor não encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> getById(
        @Parameter(description = "ID do autor", required = true)
        @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Cria um novo autor", description = "Adiciona um novo autor ao sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Autor criado com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = AuthorResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou negócio inválido", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AuthorResponseDTO> create(
        @Parameter(description = "DTO com os dados do autor", required = true) 
        @Valid @RequestBody AuthorRequestDTO dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }

    @Operation(summary = "Atualiza um autor existente", description = "Atualiza os dados de um autor pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autor atualizado com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = AuthorResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou negócio inválido", content = @Content),
        @ApiResponse(responseCode = "404", description = "Autor não encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> update(
        @Parameter(description = "ID do autor", required = true)
        @PathVariable Long id, 
        @Parameter(description = "DTO com os dados atualizados", required = true)
        @Valid @RequestBody AuthorRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Deleta um autor", description = "Remove um autor do sistema pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Autor deletado com sucesso", content = @Content),
        @ApiResponse(responseCode = "404", description = "Autor não encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @Parameter(description = "ID do autor", required = true)
        @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
