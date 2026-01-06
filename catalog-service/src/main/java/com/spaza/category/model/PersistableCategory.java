package com.spaza.category.model;

import java.util.List;

public class PersistableCategory {
    private Long id;
    private Long merchantId;
    private String code;
    private Boolean visible;
    private Boolean featured;
    private Boolean status;
    private Integer sortOrder;
    private List<CategoryDescription> descriptions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<CategoryDescription> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<CategoryDescription> descriptions) {
        this.descriptions = descriptions;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
