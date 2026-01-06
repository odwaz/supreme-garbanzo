package com.spaza.order.exception;

import lombok.Getter;

@Getter
public class PaymentFailedException extends ServiceException {
    private final String orderId;
    private final Double amount;
    
    public PaymentFailedException(String orderId, Double amount, Throwable cause) {
        super(String.format("Payment failed for order %s, amount %.2f", orderId, amount), cause);
        this.orderId = orderId;
        this.amount = amount;
    }
    
    public PaymentFailedException(String message) {
        super(message);
        this.orderId = null;
        this.amount = null;
    }
}
