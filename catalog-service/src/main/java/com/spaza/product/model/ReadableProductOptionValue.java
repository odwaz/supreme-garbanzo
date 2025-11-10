package com.spaza.product.model;

import java.util.List;

public class ReadableProductOptionValue {
    private Long id;
    private String code;
    private List<ProductOptionDescription> descriptions;
    private String image;
    private Integer sortOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ProductOptionDescription> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<ProductOptionDescription> descriptions) {
        this.descriptions = descriptions;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
