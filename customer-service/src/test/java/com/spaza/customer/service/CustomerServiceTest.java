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
        when(customerRepository.findReadableByEmail("test@example.com")).thenReturn(Optional.of(readableCustomer));
        when(jwtUtil.generateToken(anyString())).thenReturn("jwt-token-test@example.com");
        
        AuthenticationResponse result = customerService.authenticate(authRequest);

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertEquals(readableCustomer, result.getCustomer());
    }

    @Test
    void register_ShouldCreateCustomerAndReturnToken() {
        Customer savedCustomer = new Customer("John", "Doe", "test@example.com", "password");
        savedCustomer.setId(1L);
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
        when(jwtUtil.generateToken(anyString())).thenReturn("jwt-token-test@example.com");

        AuthenticationResponse result = customerService.register(customer);

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertNotNull(result.getCustomer());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void register_ShouldCreateCustomer_WhenEmailDoesNotExist() {
        Customer savedCustomer = new Customer("John", "Doe", "test@example.com", "password");
        savedCustomer.setId(1L);
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

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

        assertDoesNotThrow(() -> customerService.passwordResetRequest(request));
    }

    @Test
    void changePassword_ShouldExecuteWithoutError() {
        PasswordRequest request = new PasswordRequest();
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword");

        assertDoesNotThrow(() -> customerService.changePassword("DEFAULT", request));
    }
}