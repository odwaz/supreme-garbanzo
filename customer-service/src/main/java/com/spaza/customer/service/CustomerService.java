package com.spaza.customer.service;

import com.spaza.customer.exception.*;
import com.spaza.customer.model.*;
import com.spaza.customer.repository.CustomerRepository;
import com.spaza.customer.repository.PasswordResetTokenRepository;
import com.spaza.customer.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository tokenRepository;

    public CustomerService(CustomerRepository customerRepository,
                          JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder,
                          PasswordResetTokenRepository tokenRepository) {
        this.customerRepository = customerRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Authenticating customer: {}", request.getUsername());
        Customer customer = customerRepository.findByEmail(request.getUsername())
            .orElseThrow(() -> new AuthenticationFailedException("Invalid credentials"));
        
        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            log.warn("Failed authentication attempt for: {}", request.getUsername());
            throw new AuthenticationFailedException("Invalid credentials");
        }
        
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(jwtUtil.generateToken(request.getUsername()));
        response.setCustomer(new ReadableCustomer(customer));
        log.info("Customer authenticated successfully: {}", request.getUsername());
        return response;
    }

    @Transactional
    public AuthenticationResponse register(Customer customer) {
        log.info("Registering customer: {}", customer.getEmail());
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new ValidationException("Email already registered");
        }
        
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Customer saved = customerRepository.save(customer);
        
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(jwtUtil.generateToken(customer.getEmail()));
        response.setCustomer(new ReadableCustomer(saved));
        log.info("Customer registered successfully: {}", customer.getEmail());
        return response;
    }

    public Object refreshToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);
        log.debug("Refreshing token for: {}", email);
        return Map.of("token", jwtUtil.generateToken(email));
    }

    @Transactional
    public ResponseEntity<Void> changePassword(PasswordRequest request) {
        log.warn("Authenticated password change not implemented");
        return ResponseEntity.status(501).build();
    }

    @Transactional
    public void passwordResetRequest(ResetPasswordRequest request) {
        log.info("Password reset requested for: {}", request.getEmail());
        customerRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        
        tokenRepository.deleteByEmail(request.getEmail());
        
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(
            token, 
            request.getEmail(), 
            LocalDateTime.now().plusHours(24)
        );
        tokenRepository.save(resetToken);
        
        log.info("Password reset token generated for: {}", request.getEmail());
    }

    @Transactional
    public void resetPassword(String token, PasswordRequest request) {
        log.info("Changing password with token: {}", token);
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
            .orElseThrow(() -> new ValidationException("Invalid or expired token"));
        
        if (resetToken.isUsed()) {
            throw new ValidationException("Token already used");
        }
        if (resetToken.isExpired()) {
            throw new ValidationException("Token expired");
        }
        
        Customer customer = customerRepository.findByEmail(resetToken.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        
        customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
        customerRepository.save(customer);
        
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
        
        log.info("Password changed successfully for: {}", customer.getEmail());
    }

    public void passwordResetVerify(String token) {
        log.info("Verifying reset token: {}", token);
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
            .orElseThrow(() -> new ValidationException("Invalid token"));
        
        if (resetToken.isUsed()) {
            throw new ValidationException("Token already used");
        }
        if (resetToken.isExpired()) {
            throw new ValidationException("Token expired");
        }
        
        log.info("Token verified successfully for: {}", resetToken.getEmail());
    }

    @Transactional
    public ReadableCustomer createCustomer(Customer customer) {
        log.info("Creating customer: {}", customer.getEmail());
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        return new ReadableCustomer(customer);
    }

    public ReadableCustomer getCustomer(Long id) {
        return customerRepository.findReadableById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }

    @Transactional
    public void deleteCustomer(Long id) {
        log.info("Deleting customer: {}", id);
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found: " + id);
        }
        customerRepository.deleteById(id);
    }

    @Transactional
    public ReadableCustomer updateCustomer(Long id, Customer customer) {
        log.info("Updating customer: {}", id);
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found: " + id);
        }
        customer.setId(id);
        if (customer.getPassword() != null && !customer.getPassword().isEmpty()) {
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        }
        Customer saved = customerRepository.save(customer);
        return new ReadableCustomer(saved);
    }

    public List<ReadableCustomer> getAllCustomers() {
        return customerRepository.findAllReadable();
    }

    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Transactional
    public Customer save(Customer customer) {
        if (customer.getPassword() != null && !customer.getPassword().isEmpty()) {
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        }
        return customerRepository.save(customer);
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}