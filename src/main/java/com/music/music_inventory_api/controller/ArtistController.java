package com.music.music_inventory_api.controller;

import com.music.music_inventory_api.dto.request.CreateArtistRequest;
import com.music.music_inventory_api.dto.request.UpdateArtistRequest;
import com.music.music_inventory_api.dto.response.AlbumResponse;
import com.music.music_inventory_api.dto.response.ArtistResponse;
import com.music.music_inventory_api.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Artist endpoints. Provides CRUD operations and related
 * functionality for artists.
 */
@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Artist", description = "Artist management APIs")
public class ArtistController
{
    private final ArtistService artistService;

    /**
     * Create a new artist.
     *
     * @param request
     *            the artist creation request
     * @return the created artist
     */
    @PostMapping
    @Operation(summary = "Create a new artist", description = "Creates a new artist in the system")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "201", description = "Artist created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")})
    public ResponseEntity<ArtistResponse> createArtist(@Valid @RequestBody CreateArtistRequest request)
    {
        log.info("Creating new artist: {}", request.getName());
        ArtistResponse response = artistService.createArtist(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get artist by ID.
     *
     * @param id
     *            the artist ID
     * @return the artist
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get artist by ID", description = "Retrieves an artist by their ID")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Artist found"),
            @ApiResponse(responseCode = "404", description = "Artist not found")})
    public ResponseEntity<ArtistResponse> getArtistById(@PathVariable Long id)
    {
        log.info("Fetching artist with ID: {}", id);
        ArtistResponse response = artistService.getArtistById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all artists (paginated).
     *
     * @param pageable
     *            pagination information
     * @return page of artists
     */
    @GetMapping
    @Operation(summary = "Get all artists", description = "Retrieves all artists with pagination support")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Artists retrieved successfully")})
    public ResponseEntity<Page<ArtistResponse>> getAllArtists(Pageable pageable)
    {
        log.info("Fetching all artists with pagination: {}", pageable);
        Page<ArtistResponse> response = artistService.getAllArtists(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing artist.
     *
     * @param id
     *            the artist ID
     * @param request
     *            the artist update request
     * @return the updated artist
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update artist", description = "Updates an existing artist")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Artist updated successfully"),
            @ApiResponse(responseCode = "404", description = "Artist not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")})
    public ResponseEntity<ArtistResponse> updateArtist(@PathVariable Long id,
            @Valid @RequestBody UpdateArtistRequest request)
    {
        log.info("Updating artist with ID: {}", id);
        ArtistResponse response = artistService.updateArtist(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete an artist.
     *
     * @param id
     *            the artist ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete artist", description = "Deletes an artist by ID")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "204", description = "Artist deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Artist not found")})
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id)
    {
        log.info("Deleting artist with ID: {}", id);
        artistService.deleteArtist(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all albums by an artist.
     *
     * @param id
     *            the artist ID
     * @return list of albums
     */
    @GetMapping("/{id}/albums")
    @Operation(summary = "Get artist albums", description = "Retrieves all albums by a specific artist")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Albums retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Artist not found")})
    public ResponseEntity<List<AlbumResponse>> getArtistAlbums(@PathVariable Long id)
    {
        log.info("Fetching albums for artist with ID: {}", id);
        List<AlbumResponse> response = artistService.getAlbumsByArtist(id);
        return ResponseEntity.ok(response);
    }
}
