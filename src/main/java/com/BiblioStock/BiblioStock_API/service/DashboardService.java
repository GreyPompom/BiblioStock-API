package com.BiblioStock.BiblioStock_API.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.BiblioStock.BiblioStock_API.dto.dashboard.DashboardOverviewDTO;
import com.BiblioStock.BiblioStock_API.dto.dashboard.MovementSummaryDTO;
import com.BiblioStock.BiblioStock_API.dto.dashboard.ProductCountDTO;
import com.BiblioStock.BiblioStock_API.dto.dashboard.ProductSummaryDTO;
import com.BiblioStock.BiblioStock_API.dto.dashboard.StockValueDTO;
import com.BiblioStock.BiblioStock_API.model.Product;
import com.BiblioStock.BiblioStock_API.model.enums.MovementType;
import com.BiblioStock.BiblioStock_API.repository.CategoryRepository;
import com.BiblioStock.BiblioStock_API.repository.MovementRepository;
import com.BiblioStock.BiblioStock_API.repository.ProductRepository;

@Service
public class DashboardService {

    private final ProductService productService;
    private final MovementService movementService;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final MovementRepository movementRepository;

    public DashboardService(ProductService productService,
            MovementService movementService,
            ProductRepository productRepository,
            MovementRepository movementRepository,
            CategoryRepository categoryRepository) {
        this.productService = productService;
        this.movementService = movementService;
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
        this.categoryRepository = categoryRepository;
    }

    public DashboardOverviewDTO getOverview() {
        ProductCountDTO productCount = getTotalProducts();
        List<ProductSummaryDTO> lastProducts = getLast4Products();
        MovementSummaryDTO movementSummary = getMovementSummary();
        StockValueDTO stockValue = getTotalStockValue();

        return new DashboardOverviewDTO(
                productCount,
                lastProducts,
                movementSummary,
                stockValue
        );
    }

    public ProductCountDTO getTotalProducts() {
        long total = productRepository.count();
        long totalCategories = categoryRepository.count();
        return new ProductCountDTO(total, totalCategories);
    }

    public List<ProductSummaryDTO> getLast4Products() {
        return productRepository.findTop4ByOrderByCreatedAtDesc()
                .stream()
                .map(this::toProductSummaryDTO)
                .toList();
    }

    private ProductSummaryDTO toProductSummaryDTO(Product p) {
        return new ProductSummaryDTO(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getStockQty()
        );
    }

    public MovementSummaryDTO getMovementSummary() {
        long total = movementRepository.count();
        long entradas = movementRepository.countByMovementType(MovementType.ENTRADA.name());

        long saidas = movementRepository.countByMovementType(MovementType.SAIDA.name());

        return new MovementSummaryDTO(total, entradas, saidas);
    }
    public StockValueDTO getTotalStockValue() {
        BigDecimal total = productRepository.calculateTotalStockValue();
        return new StockValueDTO(total);

    }
}
