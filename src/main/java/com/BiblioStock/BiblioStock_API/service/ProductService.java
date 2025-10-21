package com.BiblioStock.BiblioStock_API.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.BiblioStock.BiblioStock_API.dto.ProductRequestDTO;
import com.BiblioStock.BiblioStock_API.dto.ProductResponseDTO;
import com.BiblioStock.BiblioStock_API.dto.ProductsPerCategoryDTO;
import com.BiblioStock.BiblioStock_API.exception.BusinessException;
import com.BiblioStock.BiblioStock_API.exception.ResourceNotFoundException;
import com.BiblioStock.BiblioStock_API.model.Author;
import com.BiblioStock.BiblioStock_API.model.Category;
import com.BiblioStock.BiblioStock_API.model.Product;
import com.BiblioStock.BiblioStock_API.repository.AuthorRepository;
import com.BiblioStock.BiblioStock_API.repository.CategoryRepository;
import com.BiblioStock.BiblioStock_API.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;

    public ProductService(ProductRepository productRepository,
            CategoryRepository categoryRepository,
            AuthorRepository authorRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
    }

    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponseDTO::fromEntity)
                .toList();
    }

    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id " + id));
        return ProductResponseDTO.fromEntity(product);
    }

    // Find products by category
    public List<ProductResponseDTO> findByCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Categoria não encontrada com id " + categoryId);
        }
        List<Product> products = productRepository.findByCategory_Id(categoryId);
        return products.stream()
                .map(ProductResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public ProductResponseDTO create(ProductRequestDTO dto) {
        validateBusinessRules(dto, null);

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        Set<Author> authors = new HashSet<>();
        if (dto.authorIds() != null && !dto.authorIds().isEmpty()) {
            authors = new HashSet<>(authorRepository.findAllById(dto.authorIds()));
        }

        Product product = Product.builder()
                .name(dto.name())
                .productType(dto.productType())
                .price(dto.price())
                .unit(dto.unit())
                .stockQty(dto.stockQty())
                .minQty(dto.minQty())
                .maxQty(dto.maxQty())
                .publisher(dto.publisher())
                .isbn(dto.isbn())
                .category(category)
                .authors(authors)
                .build();

        return ProductResponseDTO.fromEntity(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id " + id));

        validateBusinessRules(dto, id);

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        Set<Author> authors = new HashSet<>();
        if (dto.authorIds() != null && !dto.authorIds().isEmpty()) {
            authors = new HashSet<>(authorRepository.findAllById(dto.authorIds()));
        }

        existing.setName(dto.name());
        existing.setProductType(dto.productType());
        existing.setPrice(dto.price());
        existing.setUnit(dto.unit());
        existing.setStockQty(dto.stockQty());
        existing.setMinQty(dto.minQty());
        existing.setMaxQty(dto.maxQty());
        existing.setPublisher(dto.publisher());
        existing.setIsbn(dto.isbn());
        existing.setCategory(category);
        existing.setAuthors(authors);

        return ProductResponseDTO.fromEntity(productRepository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id " + id));
        productRepository.delete(product);
    }

    private void validateBusinessRules(ProductRequestDTO dto, Long productId) {
        // RN006 - ISBN duplicado
        if (dto.isbn() != null && productRepository.existsByIsbn(dto.isbn())) {
            if (productId == null) {
                throw new BusinessException("Já existe um produto com o mesmo ISBN.");
            }
        }

        //  - SKU duplicado
        if (dto.sku() != null && productRepository.existsBySku(dto.sku())) {
            if (productId == null) {
                throw new BusinessException("Já existe um produto com o mesmo sku.");
            }
        }

        // RN004 - Quantidade mínima <= máxima
        if (dto.maxQty() != null && dto.minQty().compareTo(dto.maxQty()) > 0) {
            throw new BusinessException("A quantidade mínima não pode ser maior que a máxima.");
        }

        // RN002 - Livro deve ter autor
        if (dto.productType().equalsIgnoreCase("Livro")
                && (dto.authorIds() == null || dto.authorIds().isEmpty())) {
            throw new BusinessException("Um produto do tipo 'Livro' deve possuir pelo menos um autor.");
        }

        // RN005 - Estoque não pode ser negativo
        if (dto.stockQty().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("O estoque não pode ser negativo.");
        }
    }

    public List<ProductsPerCategoryDTO> getProductsPerCategory() {
        return productRepository.findProductsPerCategoryRaw()
                .stream()
                .map(row -> new ProductsPerCategoryDTO(
                ((Number) row[0]).longValue(),
                (String) row[1],
                ((Number) row[2]).longValue()
        ))
                .collect(Collectors.toList());
    }

    public List<ProductsPerCategoryDTO> getProductsPerCategoryByCategoryId(Long categoryId) {
        return productRepository.findProductsPerCategoryByCategoryId(categoryId)
                .stream()
                .map(row -> new ProductsPerCategoryDTO(
                ((Number) row[0]).longValue(),
                (String) row[1],
                ((Number) row[2]).longValue()
        ))
                .collect(Collectors.toList());
    }

}
