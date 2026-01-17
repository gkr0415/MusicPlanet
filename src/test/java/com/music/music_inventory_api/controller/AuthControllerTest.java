package com.music.music_inventory_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.music_inventory_api.dto.request.LoginRequest;
import com.music.music_inventory_api.dto.request.RegisterRequest;
import com.music.music_inventory_api.dto.response.AuthResponse;
import com.music.music_inventory_api.exception.EntityNotFoundException;
import com.music.music_inventory_api.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/** Unit tests for AuthController. */
@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp()
    {
        registerRequest = RegisterRequest.builder().email("test@example.com").password("password123").firstName("John")
                .lastName("Doe").build();

        loginRequest = LoginRequest.builder().email("test@example.com").password("password123").build();

        authResponse = AuthResponse.builder().token("test-jwt-token").tokenType("Bearer").expiresIn(86400000L)
                .customerId(1L).email("test@example.com").firstName("John").lastName("Doe").build();
    }

    @Test
    void register_withValidRequest_shouldReturnCreated() throws Exception
    {
        // Arrange
        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("test-jwt-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void register_withInvalidEmail_shouldReturnBadRequest() throws Exception
    {
        // Arrange
        RegisterRequest invalidRequest = RegisterRequest.builder().email("invalid-email").password("password123")
                .firstName("John").lastName("Doe").build();

        // Act & Assert
        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))).andExpect(status().isBadRequest());
    }

    @Test
    void register_withMissingPassword_shouldReturnBadRequest() throws Exception
    {
        // Arrange
        RegisterRequest invalidRequest = RegisterRequest.builder().email("test@example.com").firstName("John")
                .lastName("Doe").build();

        // Act & Assert
        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))).andExpect(status().isBadRequest());
    }

    @Test
    void register_withShortPassword_shouldReturnBadRequest() throws Exception
    {
        // Arrange
        RegisterRequest invalidRequest = RegisterRequest.builder().email("test@example.com").password("12345")
                .firstName("John").lastName("Doe").build();

        // Act & Assert
        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))).andExpect(status().isBadRequest());
    }

    @Test
    void register_withExistingEmail_shouldReturnBadRequest() throws Exception
    {
        // Arrange
        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new IllegalArgumentException("Email already registered"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_withValidCredentials_shouldReturnOk() throws Exception
    {
        // Arrange
        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-jwt-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void login_withInvalidEmail_shouldReturnBadRequest() throws Exception
    {
        // Arrange
        LoginRequest invalidRequest = LoginRequest.builder().email("invalid-email").password("password123").build();

        // Act & Assert
        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))).andExpect(status().isBadRequest());
    }

    @Test
    void login_withMissingPassword_shouldReturnBadRequest() throws Exception
    {
        // Arrange
        LoginRequest invalidRequest = LoginRequest.builder().email("test@example.com").build();

        // Act & Assert
        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))).andExpect(status().isBadRequest());
    }

    @Test
    void login_withNonExistentEmail_shouldReturnNotFound() throws Exception
    {
        // Arrange
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new EntityNotFoundException("Customer", "email", "test@example.com"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void login_withInvalidPassword_shouldReturnUnauthorized() throws Exception
    {
        // Arrange
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new BadCredentialsException("Invalid email or password"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}
