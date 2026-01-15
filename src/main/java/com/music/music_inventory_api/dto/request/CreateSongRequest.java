package com.music.music_inventory_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO for creating a new song.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSongRequest {

    @NotBlank(message = "Song title is required")
    @Size(min = 1, max = 255, message = "Song title must be between 1 and 255 characters")
    private String title;

    @NotNull(message = "Album ID is required")
    private Long albumId;

    @NotNull(message = "Track number is required")
    @Min(value = 1, message = "Track number must be at least 1")
    private Integer trackNumber;

    @Min(value = 0, message = "Duration in seconds cannot be negative")
    private Integer durationSeconds;
}
