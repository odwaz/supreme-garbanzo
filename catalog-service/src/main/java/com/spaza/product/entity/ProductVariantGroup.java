package com.spaza.product.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PRODUCT_VARIANT_GROUP")
public class ProductVariantGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VARIANT_GROUP_ID")
    private Long id;
    
    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;
    
    @Column(name = "CODE", length = 100)
    private String code;
    
    @Column(name = "SORT_ORDER")
    private Integer sortOrder;
    
    @Transient
    private List<ProductVariation> variations = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    
    public List<ProductVariation> getVariations() { return variations; }
    public void setVariations(List<ProductVariation> variations) { this.variations = variations; }
}
