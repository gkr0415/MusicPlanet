package com.music.music_inventory_api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO for album response. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumResponse
{

    private Long id;
    private String title;
    private Long artistId;
    private String artistName;
    private LocalDate releaseDate;
    private BigDecimal price;
    private Integer stockQuantity;
    private String coverImageUrl;
    private String description;
    private Set<GenreResponse> genres;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
