package com.spaza.customer.delegate;

import com.spaza.customer.model.*;
import com.spaza.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerAuthenticationApiCtrlDelegateImpl implements CustomerAuthenticationApiCtrlDelegate {

    private final CustomerService customerService;

    public CustomerAuthenticationApiCtrlDelegateImpl(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public ResponseEntity<Object> authenticate(AuthenticationRequest authenticationRequest) {
        Object response = customerService.authenticate(authenticationRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AuthenticationResponse> register(Customer customer) {
        AuthenticationResponse response = customerService.register(customer);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<Object> refreshToken(String authHeader) {
        Object token = customerService.refreshToken(authHeader);
        return ResponseEntity.ok(token);
    }

    @Override
    public ResponseEntity<ResponseEntity> changePassword(PasswordRequest passwordRequest) {
        ResponseEntity<Void> response = customerService.changePassword(passwordRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> passwordResetRequest(ResetPasswordRequest customer) {
        customerService.passwordResetRequest(customer);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> changePassword(String store, String token, PasswordRequest passwordRequest) {
        customerService.resetPassword(token, passwordRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> passwordResetVerify(String store, String token) {
        customerService.passwordResetVerify(token);
        return ResponseEntity.ok().build();
    }
}