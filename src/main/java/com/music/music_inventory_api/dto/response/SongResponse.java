package com.music.music_inventory_api.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO for song response. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongResponse
{

    private Long id;
    private String title;
    private Long albumId;
    private Integer trackNumber;
    private Integer durationSeconds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
