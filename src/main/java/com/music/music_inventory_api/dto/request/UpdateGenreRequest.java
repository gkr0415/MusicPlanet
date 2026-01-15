package com.music.music_inventory_api.dto.request;

import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO for updating an existing genre. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGenreRequest {

    @Size(min = 1, max = 100, message = "Genre name must be between 1 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}
