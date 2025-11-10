package za.blkmarket.userauth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.blkmarket.userauth.dto.AuthRequest;
import za.blkmarket.userauth.dto.CustomerRegistration;

import javax.validation.Valid;

@Tag(name = "Customer authentication resource (Customer Authentication Api)")
public interface CustomerAuthenticationApiCtrl {

    @Operation(summary = "Authenticates a customer to the application", 
               description = "Customer can authenticate after registration")
    @PostMapping("/api/v1/customer/login")
    ResponseEntity<Object> authenticateUsingPOST(@Valid @RequestBody AuthRequest authenticationRequest);

    @Operation(summary = "Registers a customer to the application", 
               description = "Used as self-served operation")
    @PostMapping("/api/v1/customer/register")
    ResponseEntity<Object> registerUsingPOST(@Valid @RequestBody CustomerRegistration customer);

    default CustomerAuthenticationApiCtrlDelegate getDelegate() {
        return new CustomerAuthenticationApiCtrlDelegate() {};
    }
}