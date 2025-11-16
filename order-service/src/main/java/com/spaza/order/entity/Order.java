package com.spaza.order.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "SALES_ORDER")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long id;
    
    @Column(name = "CUSTOMER_ID")
    private Long customerId;
    
    @Column(name = "MERCHANT_ID")
    private Long merchantId;
    
    @Column(name = "ORDER_STATUS")
    private String status;
    
    @Column(name = "ORDER_TOTAL")
    private BigDecimal total;
    
    @Column(name = "ORDER_TAX")
    private BigDecimal tax;
    
    @Column(name = "ORDER_SHIPPING")
    private BigDecimal shipping;
    
    @Column(name = "CURRENCY_CODE")
    private String currencyCode;
    
    @Column(name = "DATE_PURCHASED")
    private LocalDateTime datePurchased;
    
    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;
    
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public BigDecimal getTax() { return tax; }
    public void setTax(BigDecimal tax) { this.tax = tax; }
    public BigDecimal getShipping() { return shipping; }
    public void setShipping(BigDecimal shipping) { this.shipping = shipping; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public LocalDateTime getDatePurchased() { return datePurchased; }
    public void setDatePurchased(LocalDateTime datePurchased) { this.datePurchased = datePurchased; }
    public LocalDateTime getDateCreated() { return dateCreated; }
    public void setDateCreated(LocalDateTime dateCreated) { this.dateCreated = dateCreated; }
    public LocalDateTime getDateModified() { return dateModified; }
    public void setDateModified(LocalDateTime dateModified) { this.dateModified = dateModified; }
}
