package com.music.music_inventory_api.controller;

import com.music.music_inventory_api.dto.request.CreateAlbumRequest;
import com.music.music_inventory_api.dto.request.UpdateAlbumRequest;
import com.music.music_inventory_api.dto.response.AlbumDetailResponse;
import com.music.music_inventory_api.dto.response.AlbumResponse;
import com.music.music_inventory_api.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Album operations. Provides endpoints for CRUD operations
 * and search capabilities.
 */
@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
@Tag(name = "Albums", description = "Album management APIs - Create, read, update, delete, and search albums")
public class AlbumController
{

    private final AlbumService albumService;

    @PostMapping
    @Operation(summary = "Create a new album", description = "Creates a new album with the provided information. "
            + "Requires valid artist ID and at least one genre ID.")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "201", description = "Album created successfully", content = @Content(schema = @Schema(implementation = AlbumResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Artist or Genre not found")})
    public ResponseEntity<AlbumResponse> createAlbum(@Valid @RequestBody CreateAlbumRequest request)
    {
        AlbumResponse response = albumService.createAlbum(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get album by ID", description = "Retrieves detailed album information including songs, artist details, and genres")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Album found", content = @Content(schema = @Schema(implementation = AlbumDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "Album not found")})
    public ResponseEntity<AlbumDetailResponse> getAlbumById(
            @Parameter(description = "Album ID", example = "1", required = true) @PathVariable Long id)
    {
        AlbumDetailResponse response = albumService.getAlbumById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all albums", description = "Retrieves all albums with pagination support. Default page size is 20.")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Albums retrieved successfully")})
    public ResponseEntity<Page<AlbumResponse>> getAllAlbums(
            @Parameter(description = "Pagination parameters (page, size, sort)") @PageableDefault(size = 20) Pageable pageable)
    {
        Page<AlbumResponse> response = albumService.getAllAlbums(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search albums", description = "Searches albums by title or artist name using a case-insensitive partial match")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Search query cannot be empty")})
    public ResponseEntity<List<AlbumResponse>> searchAlbums(
            @Parameter(description = "Search query string", example = "Beatles", required = true) @RequestParam("q") @NotBlank String query)
    {
        List<AlbumResponse> response = albumService.searchAlbums(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/genre/{genreId}")
    @Operation(summary = "Get albums by genre", description = "Retrieves all albums belonging to a specific genre")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Albums retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Genre not found")})
    public ResponseEntity<List<AlbumResponse>> getAlbumsByGenre(
            @Parameter(description = "Genre ID", example = "1", required = true) @PathVariable Long genreId)
    {
        List<AlbumResponse> response = albumService.getAlbumsByGenre(genreId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/price-range")
    @Operation(summary = "Get albums by price range", description = "Retrieves albums within a specific price range (inclusive)")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Albums retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid price range")})
    public ResponseEntity<List<AlbumResponse>> getAlbumsByPriceRange(
            @Parameter(description = "Minimum price", example = "10.00", required = true) @RequestParam("min") @NotNull @DecimalMin("0.0") BigDecimal minPrice,
            @Parameter(description = "Maximum price", example = "50.00", required = true) @RequestParam("max") @NotNull @DecimalMin("0.0") BigDecimal maxPrice)
    {
        List<AlbumResponse> response = albumService.getAlbumsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update album", description = "Updates an existing album. All fields are optional; only provided fields will be updated.")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Album updated successfully", content = @Content(schema = @Schema(implementation = AlbumResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Album, Artist, or Genre not found")})
    public ResponseEntity<AlbumResponse> updateAlbum(
            @Parameter(description = "Album ID", example = "1", required = true) @PathVariable Long id,
            @Valid @RequestBody UpdateAlbumRequest request)
    {
        AlbumResponse response = albumService.updateAlbum(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete album", description = "Deletes an album by ID. This operation cannot be undone.")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "204", description = "Album deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Album not found")})
    public ResponseEntity<Void> deleteAlbum(
            @Parameter(description = "Album ID", example = "1", required = true) @PathVariable Long id)
    {
        albumService.deleteAlbum(id);
        return ResponseEntity.noContent().build();
    }
}
