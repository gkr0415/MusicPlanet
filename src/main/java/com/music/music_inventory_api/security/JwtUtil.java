package com.music.music_inventory_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Utility class for JWT token operations including generation, validation, and
 * extraction of claims.
 */
@Component
public class JwtUtil
{

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key signingKey;

    @PostConstruct
    public void init()
    {
        // Generate a secure key from the secret string
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Extracts the username (subject) from the JWT token.
     *
     * @param token
     *            the JWT token
     * @return the username
     */
    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token
     *            the JWT token
     * @return the expiration date
     */
    public Date extractExpiration(String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param token
     *            the JWT token
     * @param claimsResolver
     *            function to extract the claim
     * @param <T>
     *            the type of the claim
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token
     *            the JWT token
     * @return all claims
     */
    private Claims extractAllClaims(String token)
    {
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
    }

    /**
     * Checks if the token has expired.
     *
     * @param token
     *            the JWT token
     * @return true if the token has expired
     */
    private Boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a JWT token for the given user details.
     *
     * @param userDetails
     *            the user details
     * @return the generated JWT token
     */
    public String generateToken(UserDetails userDetails)
    {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Generates a JWT token with custom claims for the given username.
     *
     * @param claims
     *            custom claims to include in the token
     * @param username
     *            the username (subject)
     * @return the generated JWT token
     */
    public String generateToken(Map<String, Object> claims, String username)
    {
        return createToken(claims, username);
    }

    /**
     * Creates a JWT token with the specified claims and subject.
     *
     * @param claims
     *            the claims to include
     * @param subject
     *            the subject (username)
     * @return the created JWT token
     */
    private String createToken(Map<String, Object> claims, String subject)
    {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        LOGGER.debug("Creating JWT token for user: {}", subject);

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(now).setExpiration(expiryDate)
                .signWith(signingKey, SignatureAlgorithm.HS256).compact();
    }

    /**
     * Validates the JWT token against the user details.
     *
     * @param token
     *            the JWT token
     * @param userDetails
     *            the user details to validate against
     * @return true if the token is valid
     */
    public Boolean validateToken(String token, UserDetails userDetails)
    {
        try
        {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (SignatureException e)
        {
            LOGGER.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e)
        {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e)
        {
            LOGGER.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e)
        {
            LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e)
        {
            LOGGER.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Validates the JWT token format and signature without checking against user
     * details.
     *
     * @param token
     *            the JWT token
     * @return true if the token format and signature are valid
     */
    public Boolean validateToken(String token)
    {
        try
        {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e)
        {
            LOGGER.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e)
        {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e)
        {
            LOGGER.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e)
        {
            LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e)
        {
            LOGGER.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Gets the configured token expiration time in milliseconds.
     *
     * @return expiration time in milliseconds
     */
    public long getExpirationTime()
    {
        return expiration;
    }
}
