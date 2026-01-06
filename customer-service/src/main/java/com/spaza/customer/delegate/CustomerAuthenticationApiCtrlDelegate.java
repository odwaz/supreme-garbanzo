package com.spaza.customer.delegate;

import com.spaza.customer.model.*;
import org.springframework.http.ResponseEntity;

public interface CustomerAuthenticationApiCtrlDelegate {
    ResponseEntity<Object> authenticate(AuthenticationRequest authenticationRequest);
    ResponseEntity<AuthenticationResponse> register(Customer customer);
    ResponseEntity<Object> refreshToken(String authHeader);
    ResponseEntity<ResponseEntity> changePassword(PasswordRequest passwordRequest);
    ResponseEntity<Void> passwordResetRequest(ResetPasswordRequest customer);
    ResponseEntity<Void> changePassword(String store, String token, PasswordRequest passwordRequest);
    ResponseEntity<Void> passwordResetVerify(String store, String token);
}