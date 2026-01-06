package com.spaza.payment.model;

import javax.persistence.*;

@Entity
@Table(
    name = "transactions",
    uniqueConstraints = {
        // Prevent multiple active transactions with the same status per order (e.g., duplicate PENDING)
        @UniqueConstraint(name = "uk_txn_order_status", columnNames = {"order_id", "status"})
    }
)
public class ReadableTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_id", length = 64, nullable = false)
    private String orderId;
    
    @Column(name = "amount")
    private Double amount;
    
    @Column(name = "status", length = 32, nullable = false)
    private String status;
    
    @Column(name = "payment_method", length = 32)
    private String paymentMethod;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
