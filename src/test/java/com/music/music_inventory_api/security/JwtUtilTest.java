package com.music.music_inventory_api.security;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

/** Unit tests for JwtUtil. */
class JwtUtilTest
{

    private JwtUtil jwtUtil;
    private UserDetails userDetails;

    @BeforeEach
    void setUp()
    {
        jwtUtil = new JwtUtil();

        ReflectionTestUtils.setField(jwtUtil, "secret",
                "testSecretKeyForJWTtokenGenerationMustBeLongEnough256bitsForHS256Algorithm");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
        jwtUtil.init();

        userDetails = User.builder().username("test@example.com").password("password").authorities("ROLE_CUSTOMER")
                .build();
    }

    @Test
    void generateToken_withUserDetails_shouldReturnValidToken()
    {
        // Act
        String token = jwtUtil.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3);
    }

    @Test
    void generateToken_withCustomClaims_shouldReturnValidToken()
    {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put("customClaim", "customValue");

        // Act
        String token = jwtUtil.generateToken(claims, "test@example.com");

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_withValidToken_shouldReturnUsername()
    {
        // Arrange
        String token = jwtUtil.generateToken(userDetails);

        // Act
        String username = jwtUtil.extractUsername(token);

        // Assert
        assertEquals("test@example.com", username);
    }

    @Test
    void extractExpiration_withValidToken_shouldReturnFutureDate()
    {
        // Arrange
        String token = jwtUtil.generateToken(userDetails);

        // Act
        java.util.Date expiration = jwtUtil.extractExpiration(token);

        // Assert
        assertNotNull(expiration);
        assertTrue(expiration.after(new java.util.Date()));
    }

    @Test
    void validateToken_withValidToken_shouldReturnTrue()
    {
        // Arrange
        String token = jwtUtil.generateToken(userDetails);

        // Act
        Boolean isValid = jwtUtil.validateToken(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateToken_withDifferentUser_shouldReturnFalse()
    {
        // Arrange
        String token = jwtUtil.generateToken(userDetails);
        UserDetails differentUser = User.builder().username("different@example.com").password("password")
                .authorities("ROLE_CUSTOMER").build();

        // Act
        Boolean isValid = jwtUtil.validateToken(token, differentUser);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_withInvalidToken_shouldReturnFalse()
    {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        Boolean isValid = jwtUtil.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_withValidTokenOnly_shouldReturnTrue()
    {
        // Arrange
        String token = jwtUtil.generateToken(userDetails);

        // Act
        Boolean isValid = jwtUtil.validateToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void getExpirationTime_shouldReturnConfiguredValue()
    {
        // Act
        long expirationTime = jwtUtil.getExpirationTime();

        // Assert
        assertEquals(86400000L, expirationTime);
    }

    @Test
    void validateToken_withExpiredToken_shouldReturnFalse()
    {
        // Arrange
        JwtUtil shortExpirationJwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(shortExpirationJwtUtil, "secret",
                "testSecretKeyForJWTtokenGenerationMustBeLongEnough256bitsForHS256Algorithm");
        ReflectionTestUtils.setField(shortExpirationJwtUtil, "expiration", 1L); // 1ms expiration
        shortExpirationJwtUtil.init();

        String token = shortExpirationJwtUtil.generateToken(userDetails);

        try
        {
            Thread.sleep(10);
        } catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

        // Act
        Boolean isValid = shortExpirationJwtUtil.validateToken(token);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_withTamperedToken_shouldReturnFalse()
    {
        // Arrange
        String token = jwtUtil.generateToken(userDetails);

        String tamperedToken = token.substring(0, token.length() - 5) + "XXXXX";

        // Act
        Boolean isValid = jwtUtil.validateToken(tamperedToken);

        // Assert
        assertFalse(isValid);
    }
}
