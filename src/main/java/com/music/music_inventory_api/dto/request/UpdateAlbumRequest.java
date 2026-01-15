package com.music.music_inventory_api.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO for updating an existing album. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAlbumRequest
{

    @Size(min = 1, max = 255, message = "Album title must be between 1 and 255 characters")
    private String title;

    private Long artistId;

    private LocalDate releaseDate;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    @Size(max = 255, message = "Cover image URL must not exceed 255 characters")
    private String coverImageUrl;

    private String description;

    private Set<Long> genreIds;
}
