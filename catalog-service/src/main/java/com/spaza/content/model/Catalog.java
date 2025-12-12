package com.spaza.content.model;

import javax.persistence.*;

@javax.persistence.Entity
@Table(name = "catalog")
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private Boolean visible;
    
    @Column(name = "merchant_id")
    private Long merchantId;
    
    @Column(name = "default_catalog")
    private Boolean defaultCatalog;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getVisible() { return visible; }
    public void setVisible(Boolean visible) { this.visible = visible; }

    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }

    public Boolean getDefaultCatalog() { return defaultCatalog; }
    public void setDefaultCatalog(Boolean defaultCatalog) { this.defaultCatalog = defaultCatalog; }
}
