package com.spaza.customer.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ResetPasswordRequest {
    @Email
    @NotBlank
    private String email;
    
    public ResetPasswordRequest() {}
    
    public ResetPasswordRequest(String email) {
        this.email = email;
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}