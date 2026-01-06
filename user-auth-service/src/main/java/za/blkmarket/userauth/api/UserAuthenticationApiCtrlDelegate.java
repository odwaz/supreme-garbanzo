package za.blkmarket.userauth.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.blkmarket.userauth.dto.AuthRequest;
import za.blkmarket.userauth.dto.AuthResponse;

public interface UserAuthenticationApiCtrlDelegate {

    default ResponseEntity<Object> authenticateUsingPOST1(AuthRequest authenticationRequest) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    default ResponseEntity<AuthResponse> refreshAndGetAuthenticationTokenUsingGET() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}