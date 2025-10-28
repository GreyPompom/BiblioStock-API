package com.BiblioStock.BiblioStock_API.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import com.BiblioStock.BiblioStock_API.dto.MovementRequestDTO;
import com.BiblioStock.BiblioStock_API.dto.MovementResponseDTO;
import com.BiblioStock.BiblioStock_API.service.MovementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movements")
@CrossOrigin(origins = "*")
@Tag(name = "Movements", description = "Operações relacionadas a movimentações de estoque")
public class MovementController {

    private final MovementService service;

    public MovementController(MovementService service) {
        this.service = service;
    }

    @Operation(summary = "Lista todas as movimentações", description = "Retorna uma lista com todas as movimentações registradas no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = MovementResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<MovementResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Registra uma nova movimentação", description = "Cria uma nova movimentação no estoque.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Movimentação registrada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = MovementResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada", content = @Content),
        @ApiResponse(responseCode = "404", description = "Produto ou recurso não encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<MovementResponseDTO> create(
        @Parameter(description = "DTO com os dados da movimentação", required = true)
        @Valid @RequestBody MovementRequestDTO dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }
}
