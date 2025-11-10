package com.spaza.customer.model;

import javax.validation.constraints.NotBlank;

public class PasswordRequest {
    @NotBlank
    private String currentPassword;
    
    @NotBlank
    private String newPassword;
    
    public PasswordRequest() {}
    
    public PasswordRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
    
    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
    
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}