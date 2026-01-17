package com.music.music_inventory_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO for authentication response containing JWT token. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse
{

    private String token;
    private String tokenType;
    private Long expiresIn;
    private Long customerId;
    private String email;
    private String firstName;
    private String lastName;
}
