package com.BiblioStock.BiblioStock_API.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.BiblioStock.BiblioStock_API.dto.AuthorResponseDTO;
import com.BiblioStock.BiblioStock_API.dto.CategoryResponseDTO;
import com.BiblioStock.BiblioStock_API.dto.MovementRequestDTO;
import com.BiblioStock.BiblioStock_API.dto.ProductRequestDTO;
import com.BiblioStock.BiblioStock_API.dto.ProductResponseDTO;
import com.BiblioStock.BiblioStock_API.dto.ProductsPerCategoryDTO;
import com.BiblioStock.BiblioStock_API.exception.BusinessException;
import com.BiblioStock.BiblioStock_API.exception.ResourceNotFoundException;
import com.BiblioStock.BiblioStock_API.model.Author;
import com.BiblioStock.BiblioStock_API.model.Category;
import com.BiblioStock.BiblioStock_API.model.Movement;
import com.BiblioStock.BiblioStock_API.model.Product;
import com.BiblioStock.BiblioStock_API.model.enums.MovementType;
import com.BiblioStock.BiblioStock_API.repository.AuthorRepository;
import com.BiblioStock.BiblioStock_API.repository.ProductRepository;
import com.BiblioStock.BiblioStock_API.service.SettingsService;
import com.BiblioStock.BiblioStock_API.service.AuthorService;
import com.BiblioStock.BiblioStock_API.service.CategoryService;
import com.BiblioStock.BiblioStock_API.repository.MovementRepository;
import com.BiblioStock.BiblioStock_API.model.User;
import com.BiblioStock.BiblioStock_API.service.UserService;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final SettingsService settingsService;
    private final AuthorRepository authorRepository;
    private final MovementRepository movementRepository;
    private final UserService userService;

    public ProductService(ProductRepository productRepository,
            CategoryService categoryService,
            AuthorService authorService,
            SettingsService settingsService,
            AuthorRepository authorRepository,
            MovementRepository movementRepository,
            UserService userService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.settingsService = settingsService;
        this.authorRepository = authorRepository;
        this.movementRepository = movementRepository;
        this.userService = userService;
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
        if (!categoryService.existsById(categoryId)) {
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

        CategoryResponseDTO categoryResponseDTO = categoryService.findById(dto.categoryId());

        Set<Author> authors = validateAndGetAuthors(dto);

        Product product = Product.builder()
                .name(dto.name())
                .sku(dto.sku())
                .productType(dto.productType())
                .price(dto.price())
                .unit(dto.unit())
                .stockQty(dto.stockQty())
                .minQty(dto.minQty())
                .maxQty(dto.maxQty())
                .publisher(dto.publisher())
                .isbn(dto.isbn())
                .category(categoryResponseDTO.toEntity())
                .authors(authors)
                .build();

        product.setPriceWithPercent(getAdjustedPrice(product));

        Product savedProduct = productRepository.save(product);

        productRepository.flush();
        
        if (savedProduct.getStockQty() != null && savedProduct.getStockQty().compareTo(BigDecimal.ZERO) > 0) {

            User user = userService.findByEmail("admin@livraria.com"); 

            Movement movement = Movement.builder()
                    .product(savedProduct)
                    .productNameSnapshot(savedProduct.getName())
                    .quantity(savedProduct.getStockQty()) 
                    .movementType(MovementType.ENTRADA)
                    .note("Estoque inicial no cadastro do produto")
                    .movementDate(LocalDateTime.now())
                    .user(user)
                    .build();

            movementRepository.save(movement);
        }

        return ProductResponseDTO.fromEntity(savedProduct);
    }

    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id " + id));

        validateBusinessRules(dto, id);

        CategoryResponseDTO categoryResponseDTO = categoryService.findById(dto.categoryId());
        if (categoryResponseDTO == null) {
            throw new ResourceNotFoundException("Categoria não encontrada");
        }

        Set<Author> authors = validateAndGetAuthors(dto);

        // Guarda o estoque atual antes de alterar
        BigDecimal oldStockQty = existing.getStockQty() != null ? existing.getStockQty() : BigDecimal.ZERO;
        BigDecimal newStockQty = dto.stockQty() != null ? dto.stockQty() : BigDecimal.ZERO;

        existing.setName(dto.name());
        existing.setSku(dto.sku());
        existing.setProductType(dto.productType());
        existing.setPrice(dto.price());
        existing.setUnit(dto.unit());
        existing.setMinQty(dto.minQty());
        existing.setMaxQty(dto.maxQty());
        existing.setPublisher(dto.publisher());
        existing.setIsbn(dto.isbn());
        existing.setCategory(categoryResponseDTO.toEntity());
        existing.setAuthors(authors);
        existing.setStockQty(dto.stockQty());
        existing.setPriceWithPercent(getAdjustedPrice(existing));
        Product saved = productRepository.save(existing);
        // Verifica se mudou o estoque

        // SE O ESTOQUE MUDOU, REGISTRA MOVIMENTAÇÃO
        if (newStockQty.compareTo(oldStockQty) != 0) {
            BigDecimal diff;
            MovementType movementType;

            if (newStockQty.compareTo(oldStockQty) > 0) {
                // aumentou estoque => ENTRADA
                diff = newStockQty.subtract(oldStockQty);
                movementType = MovementType.ENTRADA;
            } else {
                // diminuiu estoque => SAÍDA
                diff = oldStockQty.subtract(newStockQty);
                movementType = MovementType.SAIDA;
            }

            // só registra se diferença > 0
            if (diff.compareTo(BigDecimal.ZERO) > 0) {
                User user = userService.findByEmail("admin@livraria.com"); // depois você troca pelo usuário logado

                Movement movement = Movement.builder()
                        .product(saved)
                        .productNameSnapshot(saved.getName())
                        .quantity(diff)
                        .movementType(movementType)
                        .note("Ajuste de estoque via edição do produto")
                        .movementDate(LocalDateTime.now())
                        .user(user)
                        .build();

                movementRepository.save(movement);
            }
        }

        return ProductResponseDTO.fromEntity(saved);
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
            if (productId == null || !productRepository.findById(productId)
                    .map(existing -> existing.getIsbn().equals(dto.isbn()))
                    .orElse(false)) {
                throw new BusinessException("Já existe um produto com o mesmo ISBN.");
            }
        }

        //  - SKU duplicado
        if (dto.sku() != null && productRepository.existsBySku(dto.sku())) {
            if (productId == null || !productRepository.findById(productId)
                    .map(existing -> existing.getSku() != null && existing.getSku().equals(dto.sku()))
                    .orElse(false)) {
                throw new BusinessException("Já existe um produto com o mesmo SKU.");
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

    private Set<Author> validateAndGetAuthors(ProductRequestDTO dto) {
        Set<Author> authors = new HashSet<>();
        if (dto.productType().equalsIgnoreCase("Livro")) {
            if (dto.authorIds() == null || dto.authorIds().isEmpty()) {
                throw new BusinessException("Um produto do tipo 'Livro' deve possuir pelo menos um autor.");
            }

            // BUSCAR ENTIDADES AUTHOR DO REPOSITORY, NÃO CRIAR NOVAS
            authors = new HashSet<>(authorRepository.findAllById(dto.authorIds()));

            if (authors.size() != dto.authorIds().size()) {
                Set<Long> foundIds = authors.stream().map(Author::getId).collect(Collectors.toSet());
                Set<Long> missingIds = dto.authorIds().stream()
                        .filter(id -> !foundIds.contains(id))
                        .collect(Collectors.toSet());
                throw new ResourceNotFoundException("Autores não encontrados: " + missingIds);
            }
        }
        return authors;
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

    public BigDecimal getAdjustedPrice(Product product) {
        BigDecimal base = product.getPrice();
        Category category = product.getCategory();
        BigDecimal categoryAdj = category != null ? category.getDefaultAdjustmentPercent() : BigDecimal.ZERO;

        BigDecimal globalAdj = settingsService.getGlobalAdjustment(); // obtém do banco
        return base.multiply(BigDecimal.ONE.add(categoryAdj.add(globalAdj).divide(BigDecimal.valueOf(100))));
    }

    // RN020/RN021 - Atualizar estoque
    @Transactional
    public Product updateStock(Long productId, BigDecimal quantity, MovementType movementType) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id " + productId));

        BigDecimal newStock = product.getStockQty();

        if (movementType == MovementType.ENTRADA) {
            newStock = newStock.add(quantity);
        } else if (movementType == MovementType.SAIDA) {
            newStock = newStock.subtract(quantity);
            if (newStock.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("Estoque insuficiente para esta saída");
            }
        }

        product.setStockQty(newStock);
        return productRepository.save(product);
    }

    public boolean isCategoryLinkedToProduct(Category category) {
        return productRepository.existsByCategory(category);
    }

}
