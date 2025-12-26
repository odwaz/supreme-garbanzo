package com.spaza.product.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spaza.product.entity.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PRODUCT")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "merchant_id")
    private Long merchantId;
    
    @Column(name = "category_id")
    private Long categoryId;
    
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Boolean available;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();
    
    @Transient
    private List<ProductDescription> descriptions = new ArrayList<>();
    
    @Transient
    private List<ProductAvailability> availabilities = new ArrayList<>();
    
    @Transient
    private List<ProductVariant> variants = new ArrayList<>();
    
    @Transient
    private List<ProductVariantGroup> variantGroups = new ArrayList<>();
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
    
    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
    
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    
    public List<ProductImage> getImages() { return images; }
    public void setImages(List<ProductImage> images) { this.images = images; }
    
    public List<ProductDescription> getDescriptions() { return descriptions; }
    public void setDescriptions(List<ProductDescription> descriptions) { this.descriptions = descriptions; }
    
    public List<ProductAvailability> getAvailabilities() { return availabilities; }
    public void setAvailabilities(List<ProductAvailability> availabilities) { this.availabilities = availabilities; }
    
    public List<ProductVariant> getVariants() { return variants; }
    public void setVariants(List<ProductVariant> variants) { this.variants = variants; }
    
    public List<ProductVariantGroup> getVariantGroups() { return variantGroups; }
    public void setVariantGroups(List<ProductVariantGroup> variantGroups) { this.variantGroups = variantGroups; }
}
