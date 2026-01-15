package com.music.music_inventory_api.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO for genre response. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenreResponse
{

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
