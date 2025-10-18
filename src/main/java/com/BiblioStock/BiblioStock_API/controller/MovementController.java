package com.BiblioStock.BiblioStock_API.controller;

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
public class MovementController {

    private final MovementService service;

    public MovementController(MovementService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MovementResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<MovementResponseDTO> create(@Valid @RequestBody MovementRequestDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }
}
