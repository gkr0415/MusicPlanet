package com.music.music_inventory_api.service.impl;

import com.music.music_inventory_api.dto.request.LoginRequest;
import com.music.music_inventory_api.dto.request.RegisterRequest;
import com.music.music_inventory_api.dto.response.AuthResponse;
import com.music.music_inventory_api.entity.Customer;
import com.music.music_inventory_api.exception.EntityNotFoundException;
import com.music.music_inventory_api.repository.CustomerRepository;
import com.music.music_inventory_api.security.JwtUtil;
import com.music.music_inventory_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of AuthService for handling authentication operations. */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService
{

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request)
    {
        LOGGER.info("Registering new customer with email: {}", request.getEmail());

        // Check if email already exists
        if (customerRepository.findByEmailIgnoreCase(request.getEmail()).isPresent())
        {
            throw new IllegalArgumentException("Email already registered: " + request.getEmail());
        }

        // Create new customer
        Customer customer = Customer.builder().email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())).firstName(request.getFirstName())
                .lastName(request.getLastName()).phone(request.getPhone()).address(request.getAddress())
                .city(request.getCity()).country(request.getCountry()).postalCode(request.getPostalCode()).build();

        customer = customerRepository.save(customer);
        LOGGER.info("Successfully registered customer with ID: {}", customer.getId());

        // Generate JWT token
        UserDetails userDetails = createUserDetails(customer);
        String token = jwtUtil.generateToken(userDetails);

        return buildAuthResponse(customer, token);
    }

    @Override
    public AuthResponse login(LoginRequest request)
    {
        LOGGER.info("Login attempt for email: {}", request.getEmail());

        // Find customer by email
        Customer customer = customerRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Customer", "email", request.getEmail()));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword()))
        {
            LOGGER.warn("Invalid password attempt for email: {}", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }

        LOGGER.info("Successful login for customer ID: {}", customer.getId());

        // Generate JWT token
        UserDetails userDetails = createUserDetails(customer);
        String token = jwtUtil.generateToken(userDetails);

        return buildAuthResponse(customer, token);
    }

    /**
     * Creates UserDetails object from Customer entity.
     *
     * @param customer
     *            the customer entity
     * @return UserDetails for JWT generation
     */
    private UserDetails createUserDetails(Customer customer)
    {
        return User.builder().username(customer.getEmail()).password(customer.getPassword())
                .authorities("ROLE_CUSTOMER").build();
    }

    /**
     * Builds AuthResponse from customer and token.
     *
     * @param customer
     *            the customer entity
     * @param token
     *            the JWT token
     * @return AuthResponse with all relevant information
     */
    private AuthResponse buildAuthResponse(Customer customer, String token)
    {
        return AuthResponse.builder().token(token).tokenType("Bearer").expiresIn(jwtUtil.getExpirationTime())
                .customerId(customer.getId()).email(customer.getEmail()).firstName(customer.getFirstName())
                .lastName(customer.getLastName()).build();
    }
}
