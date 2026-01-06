package com.spaza.tax.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "TAX_RATE")
public class TaxRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAX_RATE_ID")
    private Long id;
    
    @Column(name = "CODE", length = 100)
    private String code;
    
    @Column(name = "TAX_CLASS_ID")
    private Long taxClassId;
    
    @Column(name = "COUNTRY_CODE", length = 3)
    private String countryCode;
    
    @Column(name = "STATE_PROVINCE_CODE", length = 10)
    private String stateProvinceCode;
    
    @Column(name = "TAX_RATE", precision = 7, scale = 4)
    private BigDecimal rate;
    
    @Column(name = "MERCHANT_ID")
    private Long merchantId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Long getTaxClassId() { return taxClassId; }
    public void setTaxClassId(Long taxClassId) { this.taxClassId = taxClassId; }
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public String getStateProvinceCode() { return stateProvinceCode; }
    public void setStateProvinceCode(String stateProvinceCode) { this.stateProvinceCode = stateProvinceCode; }
    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal rate) { this.rate = rate; }
    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
}
