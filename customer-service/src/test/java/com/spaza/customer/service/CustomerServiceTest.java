package com.spaza.customer.service;

import com.spaza.customer.model.*;
import com.spaza.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private com.spaza.customer.util.JwtUtil jwtUtil;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Mock
    private com.spaza.customer.repository.PasswordResetTokenRepository tokenRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private ReadableCustomer readableCustomer;
    private AuthenticationRequest authRequest;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setFirstName("John");
        customer.setLastName("Doe");

        readableCustomer = new ReadableCustomer();
        readableCustomer.setId(1L);
        readableCustomer.setEmail("test@example.com");
        readableCustomer.setFirstName("John");
        readableCustomer.setLastName("Doe");

        authRequest = new AuthenticationRequest();
        authRequest.setUsername("test@example.com");
        authRequest.setPassword("password");
    }

    @Test
    void authenticate_ShouldReturnToken() {
        customer.setId(1L);
        customer.setPassword("$2a$10$encoded");
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString())).thenReturn("jwt-token-test@example.com");
        
        AuthenticationResponse result = customerService.authenticate(authRequest);

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertNotNull(result.getCustomer());
    }

    @Test
    void register_ShouldCreateCustomerAndReturnToken() {
        customer.setPassword("password");
        Customer savedCustomer = new Customer("John", "Doe", "test@example.com", "encoded");
        savedCustomer.setId(1L);
        when(customerRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
        when(jwtUtil.generateToken("test@example.com")).thenReturn("jwt-token-test@example.com");

        AuthenticationResponse result = customerService.register(customer);

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertNotNull(result.getCustomer());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void register_ShouldCreateCustomer_WhenEmailDoesNotExist() {
        customer.setPassword("password");
        Customer savedCustomer = new Customer("John", "Doe", "test@example.com", "encoded");
        savedCustomer.setId(1L);
        when(customerRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
        when(jwtUtil.generateToken("test@example.com")).thenReturn("jwt-token");

        AuthenticationResponse result = customerService.register(customer);

        assertNotNull(result);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void refreshToken_ShouldReturnNewToken() {
        String authHeader = "Bearer test-token";
        when(jwtUtil.extractEmail("test-token")).thenReturn("test@example.com");
        when(jwtUtil.generateToken("test@example.com")).thenReturn("new-jwt-token");
        
        Object result = customerService.refreshToken(authHeader);

        assertNotNull(result);
        assertTrue(result instanceof Map);
        Map<String, String> tokenMap = (Map<String, String>) result;
        assertEquals("new-jwt-token", tokenMap.get("token"));
    }

    @Test
    void passwordResetRequest_ShouldExecuteWithoutError() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setEmail("test@example.com");
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        doNothing().when(tokenRepository).deleteByEmail(anyString());
        when(tokenRepository.save(any())).thenReturn(null);

        assertDoesNotThrow(() -> customerService.passwordResetRequest(request));
    }

    @Test
    void changePassword_ShouldExecuteWithoutError() {
        PasswordRequest request = new PasswordRequest();
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword");

        assertDoesNotThrow(() -> customerService.changePassword(request));
    }
}