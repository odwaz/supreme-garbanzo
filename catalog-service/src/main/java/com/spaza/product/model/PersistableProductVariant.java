package com.spaza.product.model;

import java.util.List;

public class PersistableProductVariant {
    private Long id;
    private String sku;
    private Integer quantity;
    private String code;
    private Boolean available;
    private String image;
    private PersistableProductPrice price;
    private List<ProductOptionDescription> options;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public PersistableProductPrice getPrice() {
        return price;
    }

    public void setPrice(PersistableProductPrice price) {
        this.price = price;
    }

    public List<ProductOptionDescription> getOptions() {
        return options;
    }

    public void setOptions(List<ProductOptionDescription> options) {
        this.options = options;
    }
}
