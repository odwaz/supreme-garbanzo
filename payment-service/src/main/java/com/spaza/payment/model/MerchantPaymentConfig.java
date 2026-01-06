package com.spaza.payment.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "merchant_payment_config")
public class MerchantPaymentConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "merchant_id")
    private Long merchantId;
    
    @Column(name = "payment_method")
    private String paymentMethod;
    
    private Boolean enabled;
    
    @Column(name = "api_key")
    private String apiKey;
    
    @Column(name = "site_code")
    private String siteCode;
    
    @Column(name = "private_key")
    private String privateKey;
    
    @Column(name = "config_json")
    private String configJson;
    
    @Column(name = "date_created")
    private LocalDateTime dateCreated;
    
    @Column(name = "date_modified")
    private LocalDateTime dateModified;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getSiteCode() { return siteCode; }
    public void setSiteCode(String siteCode) { this.siteCode = siteCode; }
    public String getPrivateKey() { return privateKey; }
    public void setPrivateKey(String privateKey) { this.privateKey = privateKey; }
    public String getConfigJson() { return configJson; }
    public void setConfigJson(String configJson) { this.configJson = configJson; }
    public LocalDateTime getDateCreated() { return dateCreated; }
    public void setDateCreated(LocalDateTime dateCreated) { this.dateCreated = dateCreated; }
    public LocalDateTime getDateModified() { return dateModified; }
    public void setDateModified(LocalDateTime dateModified) { this.dateModified = dateModified; }
}
