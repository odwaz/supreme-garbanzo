package za.blkmarket.userauth.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.blkmarket.userauth.dto.AuthRequest;
import za.blkmarket.userauth.dto.AuthResponse;

import javax.validation.Valid;

@RestController
public class UserAuthenticationApiCtrlController {

    @Autowired
    private UserAuthenticationApiCtrlDelegate delegate;

    @PostMapping("/api/v1/private/login")
    public ResponseEntity<Object> authenticateUsingPOST1(@Valid @RequestBody AuthRequest authenticationRequest) {
        return delegate.authenticateUsingPOST1(authenticationRequest);
    }

    @GetMapping("/api/v1/auth/refresh")
    public ResponseEntity<AuthResponse> refreshAndGetAuthenticationTokenUsingGET() {
        return delegate.refreshAndGetAuthenticationTokenUsingGET();
    }
}