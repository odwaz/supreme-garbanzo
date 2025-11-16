package com.spaza.customer.service;

import com.spaza.customer.model.*;
import com.spaza.customer.repository.CustomerRepository;
import com.spaza.customer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Optional<ReadableCustomer> customer = customerRepository.findReadableByEmail(request.getUsername());
        if (customer.isPresent()) {
            AuthenticationResponse response = new AuthenticationResponse();
            response.setToken(jwtUtil.generateToken(request.getUsername()));
            response.setCustomer(customer.get());
            return response;
        }
        return null;
    }

    public AuthenticationResponse register(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        
        Customer saved = customerRepository.save(customer);
        
        ReadableCustomer readableCustomer = new ReadableCustomer();
        readableCustomer.setId(saved.getId());
        readableCustomer.setFirstName(saved.getFirstName());
        readableCustomer.setLastName(saved.getLastName());
        readableCustomer.setEmail(saved.getEmail());
        
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(jwtUtil.generateToken(customer.getEmail()));
        response.setCustomer(readableCustomer);
        return response;
    }

    public Object refreshToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);
        return Map.of("token", jwtUtil.generateToken(email));
    }

    public ResponseEntity changePassword(PasswordRequest request) {
        return ResponseEntity.ok().build();
    }

    public void passwordResetRequest(ResetPasswordRequest request) {
        // Password reset logic
    }

    public void changePassword(String store, String token, PasswordRequest request) {
        // Change password logic
    }

    public void passwordResetVerify(String store, String token) {
        // Verify reset token
    }

    public ReadableCustomer createCustomer(Customer customer) {
        Customer saved = customerRepository.save(customer);
        return new ReadableCustomer(saved);
    }

    public ReadableCustomer getCustomer(Long id) {
        Optional<ReadableCustomer> customer = customerRepository.findReadableById(id);
        return customer.orElse(null);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public ReadableCustomer updateCustomer(Long id, Customer customer) {
        customer.setId(id);
        Customer saved = customerRepository.save(customer);
        return new ReadableCustomer(saved);
    }

    public List<ReadableCustomer> getAllCustomers() {
        return customerRepository.findAllReadable();
    }

    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    public List<Customer> findAll(int page, int size) {
        return customerRepository.findAll();
    }
}