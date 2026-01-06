package com.spaza.category.model;

import javax.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    
    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;
    
    @Column(name = "code", length = 100)
    private String code;
    
    @Column(name = "category_image", length = 100)
    private String image;
    
    @Column(name = "category_status")
    private Boolean status;
    
    @Column(name = "depth")
    private Integer depth;
    
    @Column(name = "featured")
    private Boolean featured;
    
    @Column(name = "lineage", length = 255)
    private String lineage;
    
    @Column(name = "sort_order")
    private Integer sortOrder;
    
    @Column(name = "visible")
    private Boolean visible;
    
    @Column(name = "parent_id")
    private Long parentId;
    
    @Column(name = "date_created")
    private java.time.LocalDateTime dateCreated;
    
    @Column(name = "date_modified")
    private java.time.LocalDateTime dateModified;
    
    @Column(name = "updt_id", length = 60)
    private String updtId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
    public Integer getDepth() { return depth; }
    public void setDepth(Integer depth) { this.depth = depth; }
    public Boolean getFeatured() { return featured; }
    public void setFeatured(Boolean featured) { this.featured = featured; }
    public String getLineage() { return lineage; }
    public void setLineage(String lineage) { this.lineage = lineage; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Boolean getVisible() { return visible; }
    public void setVisible(Boolean visible) { this.visible = visible; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public java.time.LocalDateTime getDateCreated() { return dateCreated; }
    public void setDateCreated(java.time.LocalDateTime dateCreated) { this.dateCreated = dateCreated; }
    public java.time.LocalDateTime getDateModified() { return dateModified; }
    public void setDateModified(java.time.LocalDateTime dateModified) { this.dateModified = dateModified; }
    public String getUpdtId() { return updtId; }
    public void setUpdtId(String updtId) { this.updtId = updtId; }
}
