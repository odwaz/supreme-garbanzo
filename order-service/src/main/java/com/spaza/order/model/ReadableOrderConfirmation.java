package com.spaza.order.model;

public class ReadableOrderConfirmation {
    private Long orderId;
    private String confirmationNumber;

    public ReadableOrderConfirmation(Long orderId, String confirmationNumber) {
        this.orderId = orderId;
        this.confirmationNumber = confirmationNumber;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getConfirmationNumber() { return confirmationNumber; }
    public void setConfirmationNumber(String confirmationNumber) { this.confirmationNumber = confirmationNumber; }
}
