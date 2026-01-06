package com.spaza.order.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
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
    
    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;
    
    @Column(name = "SHIPPING_ADDRESS")
    private String shippingAddress;
    
    @Column(name = "BILLING_ADDRESS")
    private String billingAddress;
    
    @Column(name = "SHIPPING_CITY")
    private String shippingCity;
    
    @Column(name = "SHIPPING_POSTAL_CODE")
    private String shippingPostalCode;
    
    @Column(name = "SHIPPING_COUNTRY")
    private String shippingCountry;
    
    @Column(name = "BILLING_CITY")
    private String billingCity;
    
    @Column(name = "BILLING_POSTAL_CODE")
    private String billingPostalCode;
    
    @Column(name = "BILLING_COUNTRY")
    private String billingCountry;
    
    @Column(name = "PHONE")
    private String phone;
    
    @Column(name = "EMAIL")
    private String email;
    
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
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }
    public String getShippingCity() { return shippingCity; }
    public void setShippingCity(String shippingCity) { this.shippingCity = shippingCity; }
    public String getShippingPostalCode() { return shippingPostalCode; }
    public void setShippingPostalCode(String shippingPostalCode) { this.shippingPostalCode = shippingPostalCode; }
    public String getShippingCountry() { return shippingCountry; }
    public void setShippingCountry(String shippingCountry) { this.shippingCountry = shippingCountry; }
    public String getBillingCity() { return billingCity; }
    public void setBillingCity(String billingCity) { this.billingCity = billingCity; }
    public String getBillingPostalCode() { return billingPostalCode; }
    public void setBillingPostalCode(String billingPostalCode) { this.billingPostalCode = billingPostalCode; }
    public String getBillingCountry() { return billingCountry; }
    public void setBillingCountry(String billingCountry) { this.billingCountry = billingCountry; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
