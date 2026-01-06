package com.spaza.customer.model;

public class AuthenticationResponse {
    private String token;
    private ReadableCustomer customer;
    
    public AuthenticationResponse() {}
    
    public AuthenticationResponse(String token, ReadableCustomer customer) {
        this.token = token;
        this.customer = customer;
    }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public ReadableCustomer getCustomer() { return customer; }
    public void setCustomer(ReadableCustomer customer) { this.customer = customer; }
}