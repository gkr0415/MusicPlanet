package com.music.music_inventory_api.service;

import com.music.music_inventory_api.dto.request.CreateAlbumRequest;
import com.music.music_inventory_api.dto.request.UpdateAlbumRequest;
import com.music.music_inventory_api.dto.response.AlbumDetailResponse;
import com.music.music_inventory_api.dto.response.AlbumResponse;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for Album business logic.
 */
public interface AlbumService
{

    /**
     * Create a new album.
     *
     * @param request
     *            the album creation request
     * @return the created album response
     */
    AlbumResponse createAlbum(CreateAlbumRequest request);

    /**
     * Get album by ID with detailed information including songs.
     *
     * @param id
     *            the album ID
     * @return the detailed album response
     */
    AlbumDetailResponse getAlbumById(Long id);

    /**
     * Get all albums with pagination.
     *
     * @param pageable
     *            pagination information
     * @return page of album responses
     */
    Page<AlbumResponse> getAllAlbums(Pageable pageable);

    /**
     * Search albums by title or artist name.
     *
     * @param searchTerm
     *            the search term
     * @return list of matching albums
     */
    List<AlbumResponse> searchAlbums(String searchTerm);

    /**
     * Get albums by genre.
     *
     * @param genreId
     *            the genre ID
     * @return list of albums in the genre
     */
    List<AlbumResponse> getAlbumsByGenre(Long genreId);

    /**
     * Get albums by price range.
     *
     * @param minPrice
     *            minimum price
     * @param maxPrice
     *            maximum price
     * @return list of albums in the price range
     */
    List<AlbumResponse> getAlbumsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Update an existing album.
     *
     * @param id
     *            the album ID
     * @param request
     *            the update request
     * @return the updated album response
     */
    AlbumResponse updateAlbum(Long id, UpdateAlbumRequest request);

    /**
     * Delete an album.
     *
     * @param id
     *            the album ID
     */
    void deleteAlbum(Long id);
}
