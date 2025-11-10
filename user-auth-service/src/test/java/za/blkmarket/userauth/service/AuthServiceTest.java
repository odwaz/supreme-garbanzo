package za.blkmarket.userauth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import za.blkmarket.userauth.dto.*;
import za.blkmarket.userauth.entity.User;
import za.blkmarket.userauth.repository.UserRepository;
import za.blkmarket.userauth.security.JwtTokenProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        
        try {
            java.lang.reflect.Field userRepoField = AuthService.class.getDeclaredField("userRepository");
            userRepoField.setAccessible(true);
            userRepoField.set(authService, userRepository);
            
            java.lang.reflect.Field encoderField = AuthService.class.getDeclaredField("passwordEncoder");
            encoderField.setAccessible(true);
            encoderField.set(authService, passwordEncoder);
            
            java.lang.reflect.Field jwtField = AuthService.class.getDeclaredField("jwtTokenProvider");
            jwtField.setAccessible(true);
            jwtField.set(authService, jwtTokenProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void authenticate_ShouldReturnAuthResponse() {
        AuthRequest request = new AuthRequest();
        request.setUsername("admin");
        request.setPassword("password");

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setEmail("admin@test.com");
        user.setPassword("encodedPassword");
        user.setActive(true);
        user.setGroups(new ArrayList<>());

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtTokenProvider.generateToken(anyString(), anyList())).thenReturn("token123");

        AuthResponse response = authService.authenticate(request);

        assertNotNull(response);
        assertEquals("token123", response.getToken());
        assertEquals("admin", response.getUsername());
    }

    @Test
    void register_ShouldCreateUser() {
        CustomerRegistration registration = new CustomerRegistration();
        registration.setFirstName("John");
        registration.setLastName("Doe");
        registration.setEmail("john@test.com");
        registration.setPassword("password");

        when(userRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(jwtTokenProvider.generateToken(anyString(), anyList())).thenReturn("token123");

        AuthResponse result = authService.register(registration);

        assertNotNull(result);
        assertEquals("token123", result.getToken());
        verify(userRepository).save(any(User.class));
    }
}
