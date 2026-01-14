package com.music.music_inventory_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for song response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongResponse {

    private Long id;
    private String title;
    private Long albumId;
    private Integer trackNumber;
    private Integer durationSeconds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
