package com.BiblioStock.BiblioStock_API.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.BiblioStock.BiblioStock_API.dto.BalanceRequestDTO;
import com.BiblioStock.BiblioStock_API.dto.BalanceResponseDTO;
import com.BiblioStock.BiblioStock_API.dto.ProductResponseDTO;
import com.BiblioStock.BiblioStock_API.dto.ProductSalesReportDTO;
import com.BiblioStock.BiblioStock_API.dto.ProductSalesSummaryDTO;
import com.BiblioStock.BiblioStock_API.dto.ProductsBellowMinimumResponseDTO;
import com.BiblioStock.BiblioStock_API.model.Product;
import com.BiblioStock.BiblioStock_API.repository.MovementRepository;
import com.BiblioStock.BiblioStock_API.repository.ProductRepository;

@Service
public class ReportsService {

    private final ProductRepository productRepository;
    private final MovementRepository movementRepository;

    public ReportsService(ProductRepository productRepository, MovementRepository movementRepository) {
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
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

        return new BalanceResponseDTO(items, totalValue);
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
            summaries = movementRepository.findProductSalesSummaryBetween(startDate, endDate);
        } else {
            summaries = movementRepository.findProductSalesSummary();
        }

        if (summaries.isEmpty()) {
            return Optional.empty();
        }

        ProductSalesSummaryDTO mostSold = summaries.get(0);                  // já ordenado desc
        ProductSalesSummaryDTO leastSold = summaries.get(summaries.size() - 1);

        if (summaries.size() == 1) {
            leastSold = mostSold; // se só tem um produto, ele é o mais e o menos vendido
        }

        ProductSalesReportDTO report = new ProductSalesReportDTO(
                mostSold,
                leastSold,
                startDate,
                endDate
        );

        return Optional.of(report);
    }
}
