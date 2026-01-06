package com.spaza.product.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUCT_PRICE")
public class ProductPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_PRICE_ID")
    private Long id;
    
    @Column(name = "PRODUCT_AVAIL_ID", nullable = false)
    private Long productAvailId;
    
    @Column(name = "PRODUCT_PRICE_AMOUNT", precision = 19, scale = 2)
    private BigDecimal productPriceAmount;
    
    @Column(name = "PRODUCT_PRICE_SPECIAL_AMOUNT", precision = 19, scale = 2)
    private BigDecimal productPriceSpecialAmount;
    
    @Column(name = "PRODUCT_PRICE_SPECIAL_START_DATE")
    private LocalDateTime productPriceSpecialStartDate;
    
    @Column(name = "PRODUCT_PRICE_SPECIAL_END_DATE")
    private LocalDateTime productPriceSpecialEndDate;
    
    @Column(name = "DEFAULT_PRICE")
    private Boolean defaultPrice;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductAvailId() { return productAvailId; }
    public void setProductAvailId(Long productAvailId) { this.productAvailId = productAvailId; }
    public BigDecimal getProductPriceAmount() { return productPriceAmount; }
    public void setProductPriceAmount(BigDecimal productPriceAmount) { this.productPriceAmount = productPriceAmount; }
    public BigDecimal getProductPriceSpecialAmount() { return productPriceSpecialAmount; }
    public void setProductPriceSpecialAmount(BigDecimal productPriceSpecialAmount) { this.productPriceSpecialAmount = productPriceSpecialAmount; }
    public LocalDateTime getProductPriceSpecialStartDate() { return productPriceSpecialStartDate; }
    public void setProductPriceSpecialStartDate(LocalDateTime productPriceSpecialStartDate) { this.productPriceSpecialStartDate = productPriceSpecialStartDate; }
    public LocalDateTime getProductPriceSpecialEndDate() { return productPriceSpecialEndDate; }
    public void setProductPriceSpecialEndDate(LocalDateTime productPriceSpecialEndDate) { this.productPriceSpecialEndDate = productPriceSpecialEndDate; }
    public Boolean getDefaultPrice() { return defaultPrice; }
    public void setDefaultPrice(Boolean defaultPrice) { this.defaultPrice = defaultPrice; }
}
