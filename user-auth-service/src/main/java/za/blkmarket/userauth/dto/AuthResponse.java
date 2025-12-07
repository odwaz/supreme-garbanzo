package za.blkmarket.userauth.dto;

import java.util.List;
import java.util.Map;

public class AuthResponse {
    private String token;
    private String username;
    private String email;
    private List<String> roles;
    private Map<String, Object> store;
    
    public AuthResponse() {}
    
    public AuthResponse(String token, String username, String email) {
        this.token = token;
        this.username = username;
        this.email = email;
    }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
    public Map<String, Object> getStore() { return store; }
    public void setStore(Map<String, Object> store) { this.store = store; }
}