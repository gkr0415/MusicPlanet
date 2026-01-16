package com.music.music_inventory_api.controller;

import com.music.music_inventory_api.dto.request.CreateSongRequest;
import com.music.music_inventory_api.dto.request.UpdateSongRequest;
import com.music.music_inventory_api.dto.response.SongResponse;
import com.music.music_inventory_api.service.SongService;
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
 * REST controller for Song endpoints. Provides CRUD operations and related
 * functionality for songs.
 */
@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Song", description = "Song management APIs")
public class SongController
{
    private final SongService songService;

    /**
     * Create a new song.
     *
     * @param request
     *            the song creation request
     * @return the created song
     */
    @PostMapping
    @Operation(summary = "Create a new song", description = "Creates a new song in the system")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "201", description = "Song created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")})
    public ResponseEntity<SongResponse> createSong(@Valid @RequestBody CreateSongRequest request)
    {
        log.info("Creating new song: {}", request.getTitle());
        SongResponse response = songService.createSong(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get song by ID.
     *
     * @param id
     *            the song ID
     * @return the song
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get song by ID", description = "Retrieves a song by its ID")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Song found"),
            @ApiResponse(responseCode = "404", description = "Song not found")})
    public ResponseEntity<SongResponse> getSongById(@PathVariable Long id)
    {
        log.info("Fetching song with ID: {}", id);
        SongResponse response = songService.getSongById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all songs with pagination.
     *
     * @param pageable
     *            pagination parameters
     * @return page of songs
     */
    @GetMapping
    @Operation(summary = "Get all songs", description = "Retrieves all songs with pagination")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Songs retrieved successfully")})
    public ResponseEntity<Page<SongResponse>> getAllSongs(Pageable pageable)
    {
        log.info("Fetching all songs with pagination");
        Page<SongResponse> response = songService.getAllSongs(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing song.
     *
     * @param id
     *            the song ID
     * @param request
     *            the song update request
     * @return the updated song
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update song", description = "Updates an existing song")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Song updated successfully"),
            @ApiResponse(responseCode = "404", description = "Song not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")})
    public ResponseEntity<SongResponse> updateSong(@PathVariable Long id, @Valid @RequestBody UpdateSongRequest request)
    {
        log.info("Updating song with ID: {}", id);
        SongResponse response = songService.updateSong(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a song.
     *
     * @param id
     *            the song ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete song", description = "Deletes a song by ID")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "204", description = "Song deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Song not found")})
    public ResponseEntity<Void> deleteSong(@PathVariable Long id)
    {
        log.info("Deleting song with ID: {}", id);
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all songs for a specific album.
     *
     * @param albumId
     *            the album ID
     * @return list of songs
     */
    @GetMapping("/album/{albumId}")
    @Operation(summary = "Get songs by album", description = "Retrieves all songs for a specific album")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Songs retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Album not found")})
    public ResponseEntity<List<SongResponse>> getSongsByAlbum(@PathVariable Long albumId)
    {
        log.info("Fetching songs for album ID: {}", albumId);
        List<SongResponse> response = songService.getSongsByAlbum(albumId);
        return ResponseEntity.ok(response);
    }
}
