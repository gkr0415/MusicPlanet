package com.music.music_inventory_api.service.impl;

import com.music.music_inventory_api.dto.request.CreateGenreRequest;
import com.music.music_inventory_api.dto.request.UpdateGenreRequest;
import com.music.music_inventory_api.dto.response.AlbumResponse;
import com.music.music_inventory_api.dto.response.GenreResponse;
import com.music.music_inventory_api.entity.Album;
import com.music.music_inventory_api.entity.Genre;
import com.music.music_inventory_api.exception.EntityNotFoundException;
import com.music.music_inventory_api.mapper.AlbumMapper;
import com.music.music_inventory_api.mapper.GenreMapper;
import com.music.music_inventory_api.repository.AlbumRepository;
import com.music.music_inventory_api.repository.GenreRepository;
import com.music.music_inventory_api.service.GenreService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of GenreService interface. Provides business logic for
 * managing genres.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class GenreServiceImpl implements GenreService
{

    private final GenreRepository genreRepository;
    private final AlbumRepository albumRepository;
    private final GenreMapper genreMapper;
    private final AlbumMapper albumMapper;

    @Override
    @Transactional
    public GenreResponse createGenre(CreateGenreRequest request)
    {
        log.info("Creating new genre with name: {}", request.getName());

        Genre genre = genreMapper.toEntity(request);
        Genre savedGenre = genreRepository.save(genre);

        log.info("Successfully created genre with ID: {}", savedGenre.getId());
        return genreMapper.toResponse(savedGenre);
    }

    @Override
    public GenreResponse getGenreById(Long id)
    {
        log.info("Fetching genre with ID: {}", id);

        Genre genre = genreRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Genre", id));

        return genreMapper.toResponse(genre);
    }

    @Override
    public Page<GenreResponse> getAllGenres(Pageable pageable)
    {
        log.info("Fetching all genres - Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Genre> genresPage = genreRepository.findAll(pageable);

        log.info("Found {} genres", genresPage.getTotalElements());
        return genresPage.map(genreMapper::toResponse);
    }

    @Override
    @Transactional
    public GenreResponse updateGenre(Long id, UpdateGenreRequest request)
    {
        log.info("Updating genre with ID: {}", id);

        Genre genre = genreRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Genre", id));

        // Update fields from request
        genreMapper.updateEntityFromRequest(request, genre);

        Genre updatedGenre = genreRepository.save(genre);

        log.info("Successfully updated genre with ID: {}", id);
        return genreMapper.toResponse(updatedGenre);
    }

    @Override
    @Transactional
    public void deleteGenre(Long id)
    {
        log.info("Deleting genre with ID: {}", id);

        if (!genreRepository.existsById(id))
        {
            throw new EntityNotFoundException("Genre", id);
        }

        genreRepository.deleteById(id);
        log.info("Successfully deleted genre with ID: {}", id);
    }

    @Override
    public List<AlbumResponse> getAlbumsByGenre(Long genreId)
    {
        log.info("Fetching albums for genre ID: {}", genreId);

        // Verify genre exists
        genreRepository.findById(genreId).orElseThrow(() -> new EntityNotFoundException("Genre", genreId));

        // Find all albums that have this genre
        List<Album> albums = albumRepository.findAll().stream()
                .filter(album -> album.getGenres().stream().anyMatch(g -> g.getId().equals(genreId)))
                .collect(Collectors.toList());

        log.info("Found {} albums for genre ID: {}", albums.size(), genreId);
        return albumMapper.toResponseList(albums);
    }
}
