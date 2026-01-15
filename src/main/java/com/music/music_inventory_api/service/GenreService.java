package com.music.music_inventory_api.service;

import com.music.music_inventory_api.dto.request.CreateGenreRequest;
import com.music.music_inventory_api.dto.request.UpdateGenreRequest;
import com.music.music_inventory_api.dto.response.AlbumResponse;
import com.music.music_inventory_api.dto.response.GenreResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for Genre entity operations. Defines business logic for
 * managing genres.
 */
public interface GenreService {

    /**
     * Create a new genre.
     *
     * @param request
     *                the genre creation request
     * @return the created genre response
     */
    GenreResponse createGenre(CreateGenreRequest request);

    /**
     * Get genre by ID.
     *
     * @param id
     *           the genre ID
     * @return the genre response
     * @throws com.music.music_inventory_api.exception.EntityNotFoundException
     *                                                                         if
     *                                                                         genre
     *                                                                         not
     *                                                                         found
     */
    GenreResponse getGenreById(Long id);

    /**
     * Get all genres with pagination.
     *
     * @param pageable
     *                 pagination information
     * @return page of genre responses
     */
    Page<GenreResponse> getAllGenres(Pageable pageable);

    /**
     * Update an existing genre.
     *
     * @param id
     *                the genre ID
     * @param request
     *                the genre update request
     * @return the updated genre response
     * @throws com.music.music_inventory_api.exception.EntityNotFoundException
     *                                                                         if
     *                                                                         genre
     *                                                                         not
     *                                                                         found
     */
    GenreResponse updateGenre(Long id, UpdateGenreRequest request);

    /**
     * Delete a genre by ID.
     *
     * @param id
     *           the genre ID
     * @throws com.music.music_inventory_api.exception.EntityNotFoundException
     *                                                                         if
     *                                                                         genre
     *                                                                         not
     *                                                                         found
     */
    void deleteGenre(Long id);

    /**
     * Get all albums by genre ID.
     *
     * @param genreId
     *                the genre ID
     * @return list of album responses
     * @throws com.music.music_inventory_api.exception.EntityNotFoundException
     *                                                                         if
     *                                                                         genre
     *                                                                         not
     *                                                                         found
     */
    List<AlbumResponse> getAlbumsByGenre(Long genreId);
}
