package com.BiblioStock.BiblioStock_API.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.BiblioStock.BiblioStock_API.dto.BalanceRequestDTO;
import com.BiblioStock.BiblioStock_API.dto.BalanceResponseDTO;
import com.BiblioStock.BiblioStock_API.repository.ProductRepository;

@Service
public class ReportsService {

    private final ProductRepository productRepository;

    public ReportsService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
}
