package za.blkmarket.userauth.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.blkmarket.userauth.dto.AuthRequest;
import za.blkmarket.userauth.dto.CustomerRegistration;

public interface CustomerAuthenticationApiCtrlDelegate {

    default ResponseEntity<Object> authenticateUsingPOST(AuthRequest authenticationRequest) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    default ResponseEntity<Object> registerUsingPOST(CustomerRegistration customer) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}