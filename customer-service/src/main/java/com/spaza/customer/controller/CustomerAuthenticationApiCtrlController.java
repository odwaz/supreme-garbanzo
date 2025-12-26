package com.spaza.customer.controller;

import com.spaza.customer.api.CustomerAuthenticationApiCtrl;
import com.spaza.customer.delegate.CustomerAuthenticationApiCtrlDelegate;
import com.spaza.customer.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CustomerAuthenticationApiCtrlController implements CustomerAuthenticationApiCtrl {

    private final CustomerAuthenticationApiCtrlDelegate delegate;

    public CustomerAuthenticationApiCtrlController(CustomerAuthenticationApiCtrlDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public ResponseEntity<Object> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return delegate.authenticate(authenticationRequest);
    }

    @Override
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody Customer customer) {
        return delegate.register(customer);
    }

    @Override
    public ResponseEntity<Object> refreshToken(@RequestHeader("Authorization") String authHeader) {
        return delegate.refreshToken(authHeader);
    }

    @Override
    public ResponseEntity<ResponseEntity> changePassword(@Valid @RequestBody PasswordRequest passwordRequest) {
        return delegate.changePassword(passwordRequest);
    }

    @Override
    public ResponseEntity<Void> passwordResetRequest(@Valid @RequestBody ResetPasswordRequest customer) {
        return delegate.passwordResetRequest(customer);
    }

    @Override
    public ResponseEntity<Void> changePassword(@PathVariable String store, @PathVariable String token, @Valid @RequestBody PasswordRequest passwordRequest) {
        return delegate.changePassword(store, token, passwordRequest);
    }

    @Override
    public ResponseEntity<Void> passwordResetVerify(@PathVariable String store, @PathVariable String token) {
        return delegate.passwordResetVerify(store, token);
    }
}