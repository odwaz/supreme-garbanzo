package com.spaza.manufacturer.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;

@Entity
@Table(name = "MANUFACTURER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MANUFACTURER_ID")
    private Long id;
    
    @Column(name = "CODE", length = 100)
    private String code;
    
    @Column(name = "MANUFACTURER_IMAGE", length = 255)
    private String image;
    
    @Column(name = "SORT_ORDER")
    private Integer sortOrder;
    
    @Column(name = "MERCHANT_ID")
    private Long merchantId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
}
