package com.BiblioStock.BiblioStock_API.controller;

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
public class PriceAdjustmentController {

    private final PriceAdjustmentService service;

    @Autowired
    public PriceAdjustmentController(PriceAdjustmentService service) {
        this.service = service;
    }

    @PostMapping("/adjust")
    public ResponseEntity<String> applyAdjustment(@Valid @RequestBody PriceAdjustmentRequestDTO dto) {
        service.applyAdjustment(dto);
        return ResponseEntity.ok("Reajuste aplicado com sucesso.");
    }

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
