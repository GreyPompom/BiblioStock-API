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


@Service
public class CategoryService {
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
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

        return CategoryResponseDTO.fromEntity(repository.save(category));
    }

    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id " + id));

        category.setName(dto.name());
        category.setSize(dto.size());
        category.setPackagingType(dto.packagingType());
        category.setDefaultAdjustmentPercent(dto.defaultAdjustmentPercent());

        return CategoryResponseDTO.fromEntity(repository.save(category));
    }

    @Transactional
    public void delete(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id " + id));
//  // Simulação de regra: impedir exclusão se tiver produtos
//         // Exemplo: if (productRepository.existsByCategory(category)) { throw ... }

//         boolean hasProducts = false; // Aqui seria a checagem real
//         if (hasProducts) {
//             throw new BusinessException("Não é possível excluir uma categoria vinculada a produtos.");
//         }
        try {
            repository.delete(category);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Não é permitido excluir a categoria pois há produtos vinculados.");
        }
    }

    
}
