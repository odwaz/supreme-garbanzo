package com.spaza.product.entity;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT_VARIATION")
public class ProductVariation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VARIATION_ID")
    private Long id;
    
    @Column(name = "VARIANT_GROUP_ID", nullable = false)
    private Long variantGroupId;
    
    @Column(name = "CODE", length = 100)
    private String code;
    
    @Column(name = "SORT_ORDER")
    private Integer sortOrder;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVariantGroupId() { return variantGroupId; }
    public void setVariantGroupId(Long variantGroupId) { this.variantGroupId = variantGroupId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
