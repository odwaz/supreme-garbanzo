package com.spaza.product.model;

import java.util.List;

public class ReadableProductType {
    private Long id;
    private String code;
    private List<ProductTypeDescription> descriptions;
    private Boolean allowAddToCart;

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

    public List<ProductTypeDescription> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<ProductTypeDescription> descriptions) {
        this.descriptions = descriptions;
    }

    public Boolean getAllowAddToCart() {
        return allowAddToCart;
    }

    public void setAllowAddToCart(Boolean allowAddToCart) {
        this.allowAddToCart = allowAddToCart;
    }
}
