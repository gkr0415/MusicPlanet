package com.music.music_inventory_api.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.music.music_inventory_api.dto.request.LoginRequest;
import com.music.music_inventory_api.dto.request.RegisterRequest;
import com.music.music_inventory_api.dto.response.AuthResponse;
import com.music.music_inventory_api.entity.Customer;
import com.music.music_inventory_api.exception.EntityNotFoundException;
import com.music.music_inventory_api.repository.CustomerRepository;
import com.music.music_inventory_api.security.JwtUtil;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Unit tests for AuthServiceImpl. */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest
{

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private Customer testCustomer;

    @BeforeEach
    void setUp()
    {
        registerRequest = RegisterRequest.builder().email("test@example.com").password("password123").firstName("John")
                .lastName("Doe").phone("1234567890").build();

        loginRequest = LoginRequest.builder().email("test@example.com").password("password123").build();

        testCustomer = Customer.builder().id(1L).email("test@example.com").password("encodedPassword").firstName("John")
                .lastName("Doe").phone("1234567890").build();
    }

    @Test
    void register_withValidRequest_shouldReturnAuthResponse()
    {
        // Arrange
        when(customerRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
        when(jwtUtil.generateToken(any())).thenReturn("test-jwt-token");
        when(jwtUtil.getExpirationTime()).thenReturn(86400000L);

        // Act
        AuthResponse response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("test-jwt-token", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(1L, response.getCustomerId());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());

        verify(customerRepository).findByEmailIgnoreCase("test@example.com");
        verify(passwordEncoder).encode("password123");
        verify(customerRepository).save(any(Customer.class));
        verify(jwtUtil).generateToken(any());
    }

    @Test
    void register_withExistingEmail_shouldThrowIllegalArgumentException()
    {
        // Arrange
        when(customerRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(testCustomer));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authService.register(registerRequest));

        assertTrue(exception.getMessage().contains("Email already registered"));
        verify(customerRepository).findByEmailIgnoreCase("test@example.com");
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void login_withValidCredentials_shouldReturnAuthResponse()
    {
        // Arrange
        when(customerRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(testCustomer));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(any())).thenReturn("test-jwt-token");
        when(jwtUtil.getExpirationTime()).thenReturn(86400000L);

        // Act
        AuthResponse response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("test-jwt-token", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(1L, response.getCustomerId());
        assertEquals("test@example.com", response.getEmail());

        verify(customerRepository).findByEmailIgnoreCase("test@example.com");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtUtil).generateToken(any());
    }

    @Test
    void login_withNonExistentEmail_shouldThrowEntityNotFoundException()
    {
        // Arrange
        when(customerRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> authService.login(loginRequest));

        verify(customerRepository).findByEmailIgnoreCase("test@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void login_withInvalidPassword_shouldThrowBadCredentialsException()
    {
        // Arrange
        when(customerRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(testCustomer));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
                () -> authService.login(loginRequest));

        assertEquals("Invalid email or password", exception.getMessage());
        verify(customerRepository).findByEmailIgnoreCase("test@example.com");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtUtil, never()).generateToken(any());
    }
}
