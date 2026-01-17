package com.music.music_inventory_api.service;

import com.music.music_inventory_api.dto.request.LoginRequest;
import com.music.music_inventory_api.dto.request.RegisterRequest;
import com.music.music_inventory_api.dto.response.AuthResponse;

/** Service interface for authentication operations. */
public interface AuthService
{

    /**
     * Registers a new customer.
     *
     * @param request
     *            the registration request containing customer details
     * @return AuthResponse with JWT token and customer info
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticates a customer and returns a JWT token.
     *
     * @param request
     *            the login request containing credentials
     * @return AuthResponse with JWT token and customer info
     */
    AuthResponse login(LoginRequest request);
}
