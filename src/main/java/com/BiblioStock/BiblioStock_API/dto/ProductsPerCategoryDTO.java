package com.BiblioStock.BiblioStock_API.dto;

public class ProductsPerCategoryDTO {

    private Long id;
    private String name;
    private Long productCount;

    public ProductsPerCategoryDTO(Long id, String name, Long productCount) {
        this.id = id;
        this.name = name;
        this.productCount = productCount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getProductCount() { return productCount; }
    public void setProductCount(Long productCount) { this.productCount = productCount; }
}

