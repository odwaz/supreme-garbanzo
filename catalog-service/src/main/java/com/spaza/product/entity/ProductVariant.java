package com.spaza.product.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCT_VARIANT")
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_VARIANT_ID")
    private Long id;
    
    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;
    
    @Column(name = "SKU", length = 255)
    private String sku;
    
    @Column(name = "CODE", length = 100)
    private String code;
    
    @Column(name = "QUANTITY")
    private Integer quantity;
    
    @Column(name = "PRICE", precision = 19, scale = 2)
    private BigDecimal price;
    
    @Column(name = "DEFAULT_SELECTION")
    private Boolean defaultSelection;
    
    @Column(name = "SORT_ORDER")
    private Integer sortOrder;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Boolean getDefaultSelection() { return defaultSelection; }
    public void setDefaultSelection(Boolean defaultSelection) { this.defaultSelection = defaultSelection; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
