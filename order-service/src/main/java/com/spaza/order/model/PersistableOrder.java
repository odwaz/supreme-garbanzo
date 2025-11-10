package com.spaza.order.model;

public class PersistableOrder {
    private Long customerId;
    private Double total;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}
