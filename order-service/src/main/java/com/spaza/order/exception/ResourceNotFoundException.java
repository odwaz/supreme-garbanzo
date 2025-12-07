package com.spaza.order.exception;

public class ResourceNotFoundException extends ServiceException {
    public ResourceNotFoundException(String resource, Long id) {
        super(String.format("%s not found with id: %s", resource, id));
    }
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
