package com.BiblioStock.BiblioStock_API.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.BiblioStock.BiblioStock_API.dto.*;
import com.BiblioStock.BiblioStock_API.model.Category;
import com.BiblioStock.BiblioStock_API.repository.CategoryRepository;
import com.BiblioStock.BiblioStock_API.exception.*;
import com.BiblioStock.BiblioStock_API.repository.ProductRepository;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final PriceAdjustmentService priceAdjustmentService;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository repository, PriceAdjustmentService priceAdjustmentService, ProductRepository productRepository) {
        this.repository = repository;
        this.priceAdjustmentService = priceAdjustmentService;
        this.productRepository = productRepository;
    }

    public List<CategoryResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(CategoryResponseDTO::fromEntity)
                .toList();
    }

    public CategoryResponseDTO findById(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id " + id));
        return CategoryResponseDTO.fromEntity(category);
    }

    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        if (repository.existsByNameIgnoreCase(dto.name())) {
            throw new BusinessException("Já existe uma categoria com esse nome.");
        }

        Category category = Category.builder()
                .name(dto.name())
                .size(dto.size())
                .packagingType(dto.packagingType())
                .defaultAdjustmentPercent(dto.defaultAdjustmentPercent())
                .build();

        Category savedCategory = repository.save(category);

        if (savedCategory == null || savedCategory.getId() == null) {
            throw new BusinessException("Erro ao criar a categoria.");
        } else {
            priceAdjustmentService.applyCategoryAdjustment(
                    dto.defaultAdjustmentPercent(),
                    category.getId(),
                    "Atualização do percentual padrão da categoria"
            );
        }

        return CategoryResponseDTO.fromEntity(savedCategory);
    }

    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto
    ) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id " + id));

        boolean adjustmentChanged = dto.defaultAdjustmentPercent() != null
                && !dto.defaultAdjustmentPercent().equals(category.getDefaultAdjustmentPercent());

        category.setName(dto.name());
        category.setSize(dto.size());
        category.setPackagingType(dto.packagingType());
        category.setDefaultAdjustmentPercent(dto.defaultAdjustmentPercent());

        if (adjustmentChanged) {
            // Chama PriceAdjustmentService para atualizar price_with_percent e registrar histórico
            priceAdjustmentService.applyCategoryAdjustment(
                    dto.defaultAdjustmentPercent(),
                    category.getId(),
                    "Atualização do percentual padrão da categoria"
            );
        }

        return CategoryResponseDTO.fromEntity(repository.save(category));
    }

    @Transactional
    public void delete(Long id
    ) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id " + id));
        // if (productRepository.existsByCategory(category)) {
        //     throw new BusinessException("Não é possível excluir uma categoria vinculada a produtos.");
        // }

        try {
            repository.delete(category);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Não é permitido excluir a categoria pois há produtos vinculados.");
        }
    }
}
