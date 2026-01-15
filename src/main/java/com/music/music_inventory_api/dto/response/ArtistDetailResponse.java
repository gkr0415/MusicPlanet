package com.music.music_inventory_api.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Detailed DTO for artist response with albums. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDetailResponse
{

    private Long id;
    private String name;
    private String country;
    private String biography;
    private List<AlbumResponse> albums;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
