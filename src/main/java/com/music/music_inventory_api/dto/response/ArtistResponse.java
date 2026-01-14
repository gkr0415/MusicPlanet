package com.music.music_inventory_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for artist response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistResponse {

    private Long id;
    private String name;
    private String country;
    private String biography;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
