package com.spaza.product.model;

import java.util.List;

public class PersistableProductVariantGroup {
    private Long id;
    private String code;
    private String name;
    private Long productId;
    private List<PersistableProductVariant> variants;
    private String image;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public List<PersistableProductVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<PersistableProductVariant> variants) {
        this.variants = variants;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
