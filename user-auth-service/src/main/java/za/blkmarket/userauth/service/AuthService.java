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
        System.out.println("AUTH: Looking for user: " + request.getUsername());
        User user = userRepository.findByEmail(request.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        System.out.println("AUTH: User found, checking password");
        System.out.println("AUTH: Stored hash: " + user.getPassword());
        System.out.println("AUTH: Input password: " + request.getPassword());
        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        System.out.println("AUTH: Password matches: " + matches);
        
        if (!matches) {
            throw new RuntimeException("Password mismatch");
        }
        
        if (!user.isActive()) {
            throw new RuntimeException("User account is disabled");
        }
        
        List<String> roles = user.getGroups() != null ? 
            user.getGroups().stream().map(group -> group.getName()).collect(Collectors.toList()) : 
            List.of();
        
        String token = jwtTokenProvider.generateToken(user.getEmail(), roles);
        
        return new AuthResponse(token, user.getEmail(), user.getEmail());
    }
    

}