package com.spaza.payment.exception;

public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
    
    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}

class ServiceException extends PaymentException {
    public ServiceException(String message) {
        super(message);
    }
}

class ResourceNotFoundException extends PaymentException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
