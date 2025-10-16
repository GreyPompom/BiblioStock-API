package com.BiblioStock.BiblioStock_API.dto;

import com.BiblioStock.BiblioStock_API.model.Category;

public class CategoryDTO {

    private Long id;
    private String name;
    private String size;
    private String packagingType;
    private Double defaultAdjustmentPercent;

    public CategoryDTO() {}

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.size = category.getSize();
        this.packagingType = category.getPackagingType();
        this.defaultAdjustmentPercent = category.getDefaultAdjustmentPercent();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSize() { return size; }
    public String getPackagingType() { return packagingType; }
    public Double getDefaultAdjustmentPercent() { return defaultAdjustmentPercent; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSize(String size) { this.size = size; }
    public void setPackagingType(String packagingType) { this.packagingType = packagingType; }
    public void setDefaultAdjustmentPercent(Double defaultAdjustmentPercent) { this.defaultAdjustmentPercent = defaultAdjustmentPercent; }
}
