package com.music.music_inventory_api.service;

import com.music.music_inventory_api.dto.request.CreateSongRequest;
import com.music.music_inventory_api.dto.request.UpdateSongRequest;
import com.music.music_inventory_api.dto.response.SongResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for Song entity operations. Defines business logic for
 * managing songs.
 */
public interface SongService {

    /**
     * Create a new song.
     *
     * @param request
     *                the song creation request
     * @return the created song response
     */
    SongResponse createSong(CreateSongRequest request);

    /**
     * Get song by ID.
     *
     * @param id
     *           the song ID
     * @return the song response
     * @throws com.music.music_inventory_api.exception.EntityNotFoundException
     *                                                                         if
     *                                                                         song
     *                                                                         not
     *                                                                         found
     */
    SongResponse getSongById(Long id);

    /**
     * Get all songs with pagination.
     *
     * @param pageable
     *                 pagination information
     * @return page of song responses
     */
    Page<SongResponse> getAllSongs(Pageable pageable);

    /**
     * Update an existing song.
     *
     * @param id
     *                the song ID
     * @param request
     *                the song update request
     * @return the updated song response
     * @throws com.music.music_inventory_api.exception.EntityNotFoundException
     *                                                                         if
     *                                                                         song
     *                                                                         not
     *                                                                         found
     */
    SongResponse updateSong(Long id, UpdateSongRequest request);

    /**
     * Delete a song by ID.
     *
     * @param id
     *           the song ID
     * @throws com.music.music_inventory_api.exception.EntityNotFoundException
     *                                                                         if
     *                                                                         song
     *                                                                         not
     *                                                                         found
     */
    void deleteSong(Long id);

    /**
     * Get all songs by album ID.
     *
     * @param albumId
     *                the album ID
     * @return list of song responses
     * @throws com.music.music_inventory_api.exception.EntityNotFoundException
     *                                                                         if
     *                                                                         album
     *                                                                         not
     *                                                                         found
     */
    List<SongResponse> getSongsByAlbum(Long albumId);
}
