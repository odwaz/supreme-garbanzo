package za.blkmarket.userauth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.blkmarket.userauth.dto.AuthRequest;
import za.blkmarket.userauth.dto.AuthResponse;

import javax.validation.Valid;

@Tag(name = "User authentication Api")
public interface UserAuthenticationApiCtrl {

    @Operation(summary = "authenticate", operationId = "authenticateUsingPOST1")
    @PostMapping("/api/v1/private/login")
    ResponseEntity<Object> authenticateUsingPOST1(@Valid @RequestBody AuthRequest authenticationRequest);

    @Operation(summary = "refreshAndGetAuthenticationToken", operationId = "refreshAndGetAuthenticationTokenUsingGET")
    @GetMapping("/api/v1/auth/refresh")
    ResponseEntity<AuthResponse> refreshAndGetAuthenticationTokenUsingGET();

    default UserAuthenticationApiCtrlDelegate getDelegate() {
        return new UserAuthenticationApiCtrlDelegate() {};
    }
}