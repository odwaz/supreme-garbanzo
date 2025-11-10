package za.blkmarket.userauth.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import za.blkmarket.userauth.dto.AuthRequest;
import za.blkmarket.userauth.dto.AuthResponse;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("${openapi.userAuthService.base-path:/auth}")
public class UserAuthenticationApiCtrlController implements UserAuthenticationApiCtrl {

    private final UserAuthenticationApiCtrlDelegate delegate;

    public UserAuthenticationApiCtrlController(@Autowired(required = false) UserAuthenticationApiCtrlDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new UserAuthenticationApiCtrlDelegate() {});
    }

    @Override
    public UserAuthenticationApiCtrlDelegate getDelegate() {
        return delegate;
    }

    @Override
    public ResponseEntity<Object> authenticateUsingPOST1(@Valid @RequestBody AuthRequest authenticationRequest) {
        return getDelegate().authenticateUsingPOST1(authenticationRequest);
    }

    @Override
    public ResponseEntity<AuthResponse> refreshAndGetAuthenticationTokenUsingGET() {
        return getDelegate().refreshAndGetAuthenticationTokenUsingGET();
    }
}