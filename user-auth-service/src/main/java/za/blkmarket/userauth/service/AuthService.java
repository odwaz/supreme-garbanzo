package za.blkmarket.userauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.blkmarket.userauth.dto.AuthRequest;
import za.blkmarket.userauth.dto.AuthResponse;

import za.blkmarket.userauth.entity.User;
import za.blkmarket.userauth.repository.UserRepository;
import za.blkmarket.userauth.security.JwtTokenProvider;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
    
    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByEmail(request.getUsername())
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()) || !user.isActive()) {
            throw new RuntimeException("Invalid credentials");
        }
        
        List<String> roles = user.getGroups() != null ? 
            user.getGroups().stream().map(group -> group.getName()).collect(Collectors.toList()) : 
            List.of();
        
        String token = jwtTokenProvider.generateToken(user.getEmail(), roles);
        
        AuthResponse response = new AuthResponse(token, user.getEmail(), user.getEmail());
        response.setRoles(roles);
        
        if (user.getMerchantStore() != null) {
            java.util.Map<String, Object> store = new java.util.HashMap<>();
            store.put("id", user.getMerchantStore().getId());
            store.put("code", user.getMerchantStore().getCode());
            store.put("name", user.getMerchantStore().getName());
            response.setStore(store);
        }
        
        return response;
    }
    

}
