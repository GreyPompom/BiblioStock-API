package com.BiblioStock.BiblioStock_API.dto;

import java.math.BigDecimal;

public class BalanceDTO {
    private Long id;
    private String name;
    private Integer stockQty;
    private BigDecimal price;
    private BigDecimal totalValue;

    public BalanceDTO(Long id, String name, Integer stockQty, BigDecimal price, BigDecimal totalValue) {
        this.id = id;
        this.name = name;
        this.stockQty = stockQty;
        this.price = price;
        this.totalValue = totalValue;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getStockQty() { return stockQty; }
    public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getTotalValue() { return totalValue; }
    public void setTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; }
}
