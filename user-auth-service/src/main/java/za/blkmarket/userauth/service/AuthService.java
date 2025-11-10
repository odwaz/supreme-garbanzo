package za.blkmarket.userauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.blkmarket.userauth.dto.AuthRequest;
import za.blkmarket.userauth.dto.AuthResponse;
import za.blkmarket.userauth.dto.CustomerRegistration;
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
    
    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        if (!user.isActive()) {
            throw new RuntimeException("User account is disabled");
        }
        
        List<String> roles = user.getGroups().stream()
            .map(group -> group.getName())
            .collect(Collectors.toList());
        
        String token = jwtTokenProvider.generateToken(user.getUsername(), roles);
        
        return new AuthResponse(token, user.getUsername(), user.getEmail());
    }
    
    public AuthResponse register(CustomerRegistration request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        
        userRepository.save(user);
        
        String token = jwtTokenProvider.generateToken(user.getUsername(), List.of("CUSTOMER"));
        return new AuthResponse(token, user.getUsername(), user.getEmail());
    }
}