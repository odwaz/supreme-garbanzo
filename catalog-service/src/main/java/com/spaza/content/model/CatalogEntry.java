package com.spaza.content.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@javax.persistence.Entity
@Table(name = "catalog_entry")
public class CatalogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "catalog_id")
    private Long catalogId;
    
    @Column(name = "product_id")
    private Long productId;
    
    @Column(name = "category_id")
    private Long categoryId;
    
    private Boolean visible;
    
    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCatalogId() { return catalogId; }
    public void setCatalogId(Long catalogId) { this.catalogId = catalogId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Boolean getVisible() { return visible; }
    public void setVisible(Boolean visible) { this.visible = visible; }

    public LocalDateTime getDateCreated() { return dateCreated; }
    public void setDateCreated(LocalDateTime dateCreated) { this.dateCreated = dateCreated; }
}
