package za.blkmarket.userauth.delegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import za.blkmarket.userauth.api.UserAuthenticationApiCtrlDelegate;
import za.blkmarket.userauth.dto.AuthRequest;
import za.blkmarket.userauth.dto.AuthResponse;
import za.blkmarket.userauth.service.AuthService;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserAuthenticationApiCtrlDelegateImpl implements UserAuthenticationApiCtrlDelegate {

    @Autowired
    private AuthService authService;

    @Override
    public ResponseEntity<Object> authenticateUsingPOST1(AuthRequest authenticationRequest) {
        try {
            AuthResponse response = authService.authenticate(authenticationRequest);
            
            // Return Shopizer-compatible response format
            Map<String, Object> shopizerResponse = new HashMap<>();
            shopizerResponse.put("token", response.getToken());
            shopizerResponse.put("username", response.getUsername());
            shopizerResponse.put("email", response.getEmail());
            
            return ResponseEntity.ok(shopizerResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Override
    public ResponseEntity<AuthResponse> refreshAndGetAuthenticationTokenUsingGET() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}