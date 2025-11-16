package com.spaza.tax.entity;

import javax.persistence.*;

@Entity
@Table(name = "TAX_CLASS")
public class TaxClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAX_CLASS_ID")
    private Long id;
    
    @Column(name = "CODE", length = 100)
    private String code;
    
    @Column(name = "TITLE", length = 255)
    private String title;
    
    @Column(name = "MERCHANT_ID")
    private Long merchantId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
}
