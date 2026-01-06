package com.spaza.product.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUCT_AVAILABILITY")
public class ProductAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_AVAIL_ID")
    private Long id;
    
    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;
    
    @Column(name = "PRODUCT_QUANTITY")
    private Integer productQuantity;
    
    @Column(name = "AVAILABLE")
    private Boolean available;
    
    @Column(name = "DATE_AVAILABLE")
    private LocalDateTime dateAvailable;
    
    @Column(name = "REGION", length = 100)
    private String region;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getProductQuantity() { return productQuantity; }
    public void setProductQuantity(Integer productQuantity) { this.productQuantity = productQuantity; }
    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
    public LocalDateTime getDateAvailable() { return dateAvailable; }
    public void setDateAvailable(LocalDateTime dateAvailable) { this.dateAvailable = dateAvailable; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
}
