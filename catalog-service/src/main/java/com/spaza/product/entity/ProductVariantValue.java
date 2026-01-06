package com.spaza.product.entity;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT_VARIANT_VALUE")
public class ProductVariantValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VARIANT_VALUE_ID")
    private Long id;
    
    @Column(name = "PRODUCT_VARIANT_ID", nullable = false)
    private Long productVariantId;
    
    @Column(name = "VARIATION_ID", nullable = false)
    private Long variationId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductVariantId() { return productVariantId; }
    public void setProductVariantId(Long productVariantId) { this.productVariantId = productVariantId; }
    public Long getVariationId() { return variationId; }
    public void setVariationId(Long variationId) { this.variationId = variationId; }
}
