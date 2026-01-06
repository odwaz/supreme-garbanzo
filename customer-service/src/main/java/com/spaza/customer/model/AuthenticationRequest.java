package com.spaza.customer.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import javax.validation.constraints.NotBlank;

public class AuthenticationRequest {
    @NotBlank
    @JsonAlias({"email", "username"})
    private String username;
    
    @NotBlank
    private String password;
    
    public AuthenticationRequest() {}
    
    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}