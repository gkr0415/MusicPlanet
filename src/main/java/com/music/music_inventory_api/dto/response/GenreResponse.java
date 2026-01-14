package com.music.music_inventory_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for genre response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenreResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
