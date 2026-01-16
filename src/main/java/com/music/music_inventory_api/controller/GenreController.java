package com.music.music_inventory_api.controller;

import com.music.music_inventory_api.dto.request.CreateGenreRequest;
import com.music.music_inventory_api.dto.request.UpdateGenreRequest;
import com.music.music_inventory_api.dto.response.AlbumResponse;
import com.music.music_inventory_api.dto.response.GenreResponse;
import com.music.music_inventory_api.service.GenreService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Genre endpoints. Provides CRUD operations and related
 * functionality for genres.
 */
@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Genre", description = "Genre management APIs")
public class GenreController
{
    private final GenreService genreService;

    /**
     * Create a new genre.
     *
     * @param request
     *            the genre creation request
     * @return the created genre
     */
    @PostMapping
    @Operation(summary = "Create a new genre", description = "Creates a new genre in the system")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "201", description = "Genre created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")})
    public ResponseEntity<GenreResponse> createGenre(@Valid @RequestBody CreateGenreRequest request)
    {
        log.info("Creating new genre: {}", request.getName());
        GenreResponse response = genreService.createGenre(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get genre by ID.
     *
     * @param id
     *            the genre ID
     * @return the genre
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get genre by ID", description = "Retrieves a genre by its ID")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Genre found"),
            @ApiResponse(responseCode = "404", description = "Genre not found")})
    public ResponseEntity<GenreResponse> getGenreById(@PathVariable Long id)
    {
        log.info("Fetching genre with ID: {}", id);
        GenreResponse response = genreService.getGenreById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all genres with pagination.
     *
     * @param pageable
     *            pagination parameters
     * @return page of genres
     */
    @GetMapping
    @Operation(summary = "Get all genres", description = "Retrieves all genres with pagination")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Genres retrieved successfully")})
    public ResponseEntity<Page<GenreResponse>> getAllGenres(Pageable pageable)
    {
        log.info("Fetching all genres with pagination");
        Page<GenreResponse> response = genreService.getAllGenres(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing genre.
     *
     * @param id
     *            the genre ID
     * @param request
     *            the genre update request
     * @return the updated genre
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update genre", description = "Updates an existing genre")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Genre updated successfully"),
            @ApiResponse(responseCode = "404", description = "Genre not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")})
    public ResponseEntity<GenreResponse> updateGenre(@PathVariable Long id,
            @Valid @RequestBody UpdateGenreRequest request)
    {
        log.info("Updating genre with ID: {}", id);
        GenreResponse response = genreService.updateGenre(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a genre.
     *
     * @param id
     *            the genre ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete genre", description = "Deletes a genre by ID")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "204", description = "Genre deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Genre not found")})
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id)
    {
        log.info("Deleting genre with ID: {}", id);
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all albums for a specific genre.
     *
     * @param id
     *            the genre ID
     * @return list of albums
     */
    @GetMapping("/{id}/albums")
    @Operation(summary = "Get albums by genre", description = "Retrieves all albums for a specific genre")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Albums retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Genre not found")})
    public ResponseEntity<List<AlbumResponse>> getAlbumsByGenre(@PathVariable Long id)
    {
        log.info("Fetching albums for genre ID: {}", id);
        List<AlbumResponse> response = genreService.getAlbumsByGenre(id);
        return ResponseEntity.ok(response);
    }
}
