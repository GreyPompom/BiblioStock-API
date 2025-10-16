package com.BiblioStock.BiblioStock_API.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.BiblioStock.BiblioStock_API.dto.CategoryDTO;
import com.BiblioStock.BiblioStock_API.model.Category;
import com.BiblioStock.BiblioStock_API.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<CategoryDTO> findAll() {
        return repository.findAll().stream().map(CategoryDTO::new).collect(Collectors.toList());
    }
    
     public CategoryDTO findById(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com id " + id));
        return new CategoryDTO(category);
    }

    public CategoryDTO create(CategoryDTO dto) {
        if (repository.existsByName(dto.getName())) {
            throw new RuntimeException("Já existe uma categoria com este nome.");
        }

        Category category = new Category();
        category.setName(dto.getName());
        category.setSize(dto.getSize());
        category.setPackagingType(dto.getPackagingType());
        category.setDefaultAdjustmentPercent(dto.getDefaultAdjustmentPercent());

        return new CategoryDTO(repository.save(category));
    }

    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com id " + id));

        category.setName(dto.getName());
        category.setSize(dto.getSize());
        category.setPackagingType(dto.getPackagingType());
        category.setDefaultAdjustmentPercent(dto.getDefaultAdjustmentPercent());

        return new CategoryDTO(repository.save(category));
    }
    public void delete(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com id " + id));
        
        try {
            repository.delete(category);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Não é permitido excluir a categoria pois há produtos vinculados.");
        }
    }
}
