package com.BiblioStock.BiblioStock_API.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProductsPerCategoryDTO", description = "DTO para relatório de quantidade de produtos por categoria")
public class ProductsPerCategoryDTO {

    @Schema(description = "ID da categoria", example = "1", required = true)
    private Long id;

    @Schema(description = "Nome da categoria", example = "Livros", required = true)
    private String name;

    @Schema(description = "Quantidade de produtos na categoria", example = "25", required = true)
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

