package com.spaza.customer.api;

import com.spaza.customer.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface CustomerAuthenticationApiCtrl {

    @PostMapping("/api/v1/customer/login")
    ResponseEntity<Object> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest);

    @PostMapping("/api/v1/customer/register")
    ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody Customer customer);

    @GetMapping("/api/v1/auth/customer/refresh")
    ResponseEntity<Object> refreshToken(@RequestHeader("Authorization") String authHeader);

    @PostMapping("/api/v1/auth/customer/password")
    ResponseEntity<ResponseEntity> changePassword(@Valid @RequestBody PasswordRequest passwordRequest);

    @PostMapping("/api/v1/customer/password/reset/request")
    ResponseEntity<Void> passwordResetRequest(@Valid @RequestBody ResetPasswordRequest customer);

    @PostMapping("/api/v1/customer/{store}/password/{token}")
    ResponseEntity<Void> changePassword(@PathVariable String store, @PathVariable String token, @Valid @RequestBody PasswordRequest passwordRequest);

    @GetMapping("/api/v1/customer/{store}/reset/{token}")
    ResponseEntity<Void> passwordResetVerify(@PathVariable String store, @PathVariable String token);
}