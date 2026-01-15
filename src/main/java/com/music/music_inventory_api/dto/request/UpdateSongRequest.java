package com.music.music_inventory_api.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO for updating an existing song. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSongRequest {

    @Size(min = 1, max = 255, message = "Song title must be between 1 and 255 characters")
    private String title;

    private Long albumId;

    @Min(value = 1, message = "Track number must be at least 1")
    private Integer trackNumber;

    @Min(value = 0, message = "Duration in seconds cannot be negative")
    private Integer durationSeconds;
}
