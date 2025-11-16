package za.blkmarket.userauth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.blkmarket.userauth.dto.AuthRequest;
import za.blkmarket.userauth.dto.AuthResponse;

import za.blkmarket.userauth.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "User authentication Api", description = "Shopizer-compatible authentication endpoints")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    @Operation(summary = "Authenticates a customer to the application", 
               description = "Customer can authenticate after registration")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }
    

    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("User Auth Service is running");
    }
    
    @GetMapping("/hash/{password}")
    public ResponseEntity<String> hash(@PathVariable String password) {
        return ResponseEntity.ok(authService.hashPassword(password));
    }
}