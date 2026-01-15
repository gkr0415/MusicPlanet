package com.music.music_inventory_api.service;

import com.music.music_inventory_api.dto.request.CreateArtistRequest;
import com.music.music_inventory_api.dto.request.UpdateArtistRequest;
import com.music.music_inventory_api.dto.response.AlbumResponse;
import com.music.music_inventory_api.dto.response.ArtistResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for Artist entity operations. Defines business logic for
 * managing artists.
 */
public interface ArtistService
{

    /**
     * Create a new artist.
     *
     * @param request
     *            the artist creation request
     * @return the created artist response
     */
    ArtistResponse createArtist(CreateArtistRequest request);

    /**
     * Get artist by ID.
     *
     * @param id
     *            the artist ID
     * @return the artist response
     * @throws com.music.music_inventory_api.exception.EntityNotFoundException
     *             if artist not found
     */
    ArtistResponse getArtistById(Long id);

    /**
     * Get all artists with pagination.
     *
     * @param pageable
     *            pagination information
     * @return page of artist responses
     */
    Page<ArtistResponse> getAllArtists(Pageable pageable);

    /**
     * Update an existing artist.
     *
     * @param id
     *            the artist ID
     * @param request
     *            the artist update request
     * @return the updated artist response
     * @throws com.music.music_inventory_api.exception.EntityNotFoundException
     *             if artist not found
     */
    ArtistResponse updateArtist(Long id, UpdateArtistRequest request);

    /**
     * Delete an artist by ID.
     *
     * @param id
     *            the artist ID
     * @throws com.music.music_inventory_api.exception.EntityNotFoundException
     *             if artist not found
     */
    void deleteArtist(Long id);

    /**
     * Get all albums by artist ID.
     *
     * @param artistId
     *            the artist ID
     * @return list of album responses
     * @throws com.music.music_inventory_api.exception.EntityNotFoundException
     *             if artist not found
     */
    List<AlbumResponse> getAlbumsByArtist(Long artistId);
}
