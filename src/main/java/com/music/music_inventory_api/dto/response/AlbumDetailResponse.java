package com.music.music_inventory_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Detailed DTO for album response with songs and full artist information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDetailResponse {

    private Long id;
    private String title;
    private ArtistResponse artist;
    private LocalDate releaseDate;
    private BigDecimal price;
    private Integer stockQuantity;
    private String coverImageUrl;
    private String description;
    private Set<GenreResponse> genres;
    private List<SongResponse> songs;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
