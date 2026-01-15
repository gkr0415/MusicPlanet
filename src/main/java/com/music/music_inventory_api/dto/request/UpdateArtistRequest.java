package com.music.music_inventory_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

/**
 * DTO for updating an existing artist.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArtistRequest {

    @Size(min = 1, max = 255, message = "Artist name must be between 1 and 255 characters")
    private String name;

    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    private String biography;
}
