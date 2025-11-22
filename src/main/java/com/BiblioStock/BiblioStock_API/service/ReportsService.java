package com.BiblioStock.BiblioStock_API.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.BiblioStock.BiblioStock_API.dto.ProductResponseDTO;
import com.BiblioStock.BiblioStock_API.dto.reports.*;
import com.BiblioStock.BiblioStock_API.model.Product;
import com.BiblioStock.BiblioStock_API.service.ProductService;
import com.BiblioStock.BiblioStock_API.repository.MovementRepository;
import com.BiblioStock.BiblioStock_API.repository.ProductRepository;

@Service
public class ReportsService {

    private final ProductRepository productRepository;
    private final MovementRepository movementRepository;
    private final ProductService productService;

    public ReportsService(ProductRepository productRepository, MovementRepository movementRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
        this.productService = productService;
    }

    public List<BalanceRequestDTO> getBalance() {
        List<Object[]> results = productRepository.findBalance();
        List<BalanceRequestDTO> balanceList = new ArrayList<>();

        for (Object[] row : results) {
            Long id = ((Number) row[0]).longValue();
            String name = (String) row[1];
            Integer stockQty = ((Number) row[2]).intValue();
            BigDecimal price = (BigDecimal) row[3];
            BigDecimal totalValue = (BigDecimal) row[4];

            balanceList.add(new BalanceRequestDTO(id, name, stockQty, price, totalValue));
        }

        return balanceList;
    }

    public BigDecimal getTotalInventoryValue() {
        return getBalance().stream()
                .map(BalanceRequestDTO::totalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BalanceResponseDTO getBalanceReport() {
        List<BalanceRequestDTO> items = getBalance();
        BigDecimal totalValue = items.stream()
                .map(BalanceRequestDTO::totalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return new BalanceResponseDTO(items, totalValue, BigDecimal.ZERO);
    }

    public List<ProductsBellowMinimumResponseDTO> getProductsBellowMinimum() {
        List<Object[]> results = productRepository.findProductsBellowMinimum();
        List<ProductsBellowMinimumResponseDTO> responseList = new ArrayList<>();

        for (Object[] row : results) {
            // Mapear cada coluna manualmente
            Long productId = ((Number) row[0]).longValue();
            String productName = (String) row[1];
            String categoryName = (String) row[2];
            BigDecimal minQTD = (BigDecimal) row[3];
            BigDecimal stockQTD = (BigDecimal) row[4];
            BigDecimal deficit = (BigDecimal) row[5];

            ProductsBellowMinimumResponseDTO dto = new ProductsBellowMinimumResponseDTO(
                    productId,
                    productName,
                    categoryName,
                    minQTD,
                    stockQTD,
                    deficit
            );

            responseList.add(dto);
        }

        return responseList;

    }

    public Optional<ProductSalesReportDTO> getSalesReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<ProductSalesSummaryDTO> summaries;

        if (startDate != null && endDate != null) {
            summaries = movementRepository.findProductSalesSummaryBetweenNative(startDate, endDate)
                    .stream()
                    .map(p -> new ProductSalesSummaryDTO(
                            p.getProductId(),
                            p.getProductName(),
                            p.getTotalQuantity()
                    ))
                    .toList();
        } else {
            summaries =  movementRepository.findProductSalesSummaryNative()
                    .stream()
                    .map(p -> new ProductSalesSummaryDTO(
                            p.getProductId(),
                            p.getProductName(),
                            p.getTotalQuantity()
                    ))
                    .toList();
        }

        if (summaries.isEmpty()) {
            return Optional.empty();
        }

        ProductSalesSummaryDTO mostSold = summaries.get(0);                  // já ordenado desc
        ProductSalesSummaryDTO leastSold = summaries.get(summaries.size() - 1);

        if (summaries.size() == 1) {
            leastSold = mostSold;
        }

        ProductSalesReportDTO report = new ProductSalesReportDTO(
                mostSold,
                leastSold,
                startDate,
                endDate
        );

        return Optional.of(report);
    }

   public MovementsHistoryReportDTO getMovementsHistoryReport() {
        List<MovementHistoryItemDTO> movements =
                movementRepository.findMovementHistoryNative()
                        .stream()
                        .map(p -> new MovementHistoryItemDTO(
                                p.getProductId(),
                                p.getProductNameSnapshot(),
                                p.getTotalEntrada(),
                                p.getTotalSaida(),
                                p.getSaldo()
                        ))
                        .toList();
         List<ProductSalesSummaryDTO> summaries =
            movementRepository.findProductSalesSummaryNative()
                    .stream()
                    .map(p -> new ProductSalesSummaryDTO(
                            p.getProductId(),
                            p.getProductName(),
                            p.getTotalQuantity()
                    ))
                    .toList();

        ProductSalesSummaryDTO mostSold = null;
        ProductSalesSummaryDTO leastSold = null;

        if (!summaries.isEmpty()) {
            mostSold = summaries.get(0);
            leastSold = summaries.get(summaries.size() - 1);

            if (summaries.size() == 1) {
                leastSold = mostSold;
            }
        }

        return new MovementsHistoryReportDTO(movements, mostSold, leastSold);
    }

    public List<ProductPricesDTO> getProductPricesReport() {
        List<ProductResponseDTO> products = productService.findAll();
        List<ProductPricesDTO> productPrices = new ArrayList<>();

        for (ProductResponseDTO product : products) {
            ProductPricesDTO dto = new ProductPricesDTO(
                    product.id(),
                    product.name(),
                    product.isbn(),
                    product.price(),
                    product.priceWithPercent()
            );
            productPrices.add(dto);
        }
        return productPrices;
    }
    
}
