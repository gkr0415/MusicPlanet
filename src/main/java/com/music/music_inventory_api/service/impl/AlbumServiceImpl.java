package com.music.music_inventory_api.service.impl;

import com.music.music_inventory_api.dto.request.CreateAlbumRequest;
import com.music.music_inventory_api.dto.request.UpdateAlbumRequest;
import com.music.music_inventory_api.dto.response.AlbumDetailResponse;
import com.music.music_inventory_api.dto.response.AlbumResponse;
import com.music.music_inventory_api.dto.response.SongResponse;
import com.music.music_inventory_api.entity.Album;
import com.music.music_inventory_api.entity.Artist;
import com.music.music_inventory_api.entity.Genre;
import com.music.music_inventory_api.entity.Song;
import com.music.music_inventory_api.exception.EntityNotFoundException;
import com.music.music_inventory_api.mapper.AlbumMapper;
import com.music.music_inventory_api.mapper.SongMapper;
import com.music.music_inventory_api.repository.AlbumRepository;
import com.music.music_inventory_api.repository.ArtistRepository;
import com.music.music_inventory_api.repository.GenreRepository;
import com.music.music_inventory_api.repository.SongRepository;
import com.music.music_inventory_api.service.AlbumService;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of AlbumService.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SuppressWarnings("null")
public class AlbumServiceImpl implements AlbumService
{

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    private final SongRepository songRepository;
    private final AlbumMapper albumMapper;
    private final SongMapper songMapper;

    @Override
    @Transactional
    public AlbumResponse createAlbum(CreateAlbumRequest request)
    {
        log.debug("Creating album: {}", request.getTitle());

        // Validate and fetch artist
        Artist artist = artistRepository.findById(request.getArtistId())
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with ID: " + request.getArtistId()));

        // Validate and fetch genres
        Set<Genre> genres = new HashSet<>();
        if (request.getGenreIds() != null)
        {
            for (Long genreId : request.getGenreIds())
            {
                Genre genre = genreRepository.findById(genreId)
                        .orElseThrow(() -> new EntityNotFoundException("Genre not found with ID: " + genreId));
                genres.add(genre);
            }
        }

        // Map request to entity
        Album album = albumMapper.toEntity(request);
        album.setArtist(artist);

        // Set genres (bidirectional relationship)
        for (Genre genre : genres)
        {
            album.addGenre(genre);
        }

        // Save album
        Album savedAlbum = albumRepository.save(album);
        log.info("Created album with ID: {}", savedAlbum.getId());

        return albumMapper.toResponse(savedAlbum);
    }

    @Override
    public AlbumDetailResponse getAlbumById(Long id)
    {
        log.debug("Fetching album by ID: {}", id);

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with ID: " + id));

        // Fetch songs for this album
        List<Song> songs = songRepository.findByAlbumId(id);
        List<SongResponse> songResponses = songMapper.toResponseList(songs);

        // Map to detail response
        AlbumDetailResponse response = albumMapper.toDetailResponse(album);
        response.setSongs(songResponses);

        return response;
    }

    @Override
    public Page<AlbumResponse> getAllAlbums(Pageable pageable)
    {
        log.debug("Fetching all albums with pagination: {}", pageable);

        Page<Album> albumPage = albumRepository.findAll(pageable);
        return albumPage.map(albumMapper::toResponse);
    }

    @Override
    public List<AlbumResponse> searchAlbums(String searchTerm)
    {
        log.debug("Searching albums with term: {}", searchTerm);

        if (searchTerm == null || searchTerm.trim().isEmpty())
        {
            throw new IllegalArgumentException("Search term cannot be empty");
        }

        List<Album> albums = albumRepository.searchByTitleOrArtistName(searchTerm);
        return albumMapper.toResponseList(albums);
    }

    @Override
    public List<AlbumResponse> getAlbumsByGenre(Long genreId)
    {
        log.debug("Fetching albums by genre ID: {}", genreId);

        // Fetch genre to get its name
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with ID: " + genreId));

        List<Album> albums = albumRepository.searchByGenreAndPriceRange(genre.getName(), BigDecimal.ZERO,
                new BigDecimal("99999.99"));
        return albumMapper.toResponseList(albums);
    }

    @Override
    public List<AlbumResponse> getAlbumsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice)
    {
        log.debug("Fetching albums by price range: {} - {}", minPrice, maxPrice);

        if (minPrice == null || maxPrice == null)
        {
            throw new IllegalArgumentException("Price range cannot be null");
        }

        if (minPrice.compareTo(maxPrice) > 0)
        {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }

        List<Album> albums = albumRepository.findAll().stream()
                .filter(album -> album.getPrice().compareTo(minPrice) >= 0 && album.getPrice().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());

        return albumMapper.toResponseList(albums);
    }

    @Override
    @Transactional
    public AlbumResponse updateAlbum(Long id, UpdateAlbumRequest request)
    {
        log.debug("Updating album with ID: {}", id);

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with ID: " + id));

        // Use mapper to update entity from request
        albumMapper.updateEntityFromRequest(request, album);

        // Update genres if provided
        if (request.getGenreIds() != null && !request.getGenreIds().isEmpty())
        {
            // Clear existing genres
            album.getGenres().clear();

            // Add new genres
            Set<Genre> newGenres = new HashSet<>();
            for (Long genreId : request.getGenreIds())
            {
                Genre genre = genreRepository.findById(genreId)
                        .orElseThrow(() -> new EntityNotFoundException("Genre not found with ID: " + genreId));
                newGenres.add(genre);
            }

            for (Genre genre : newGenres)
            {
                album.addGenre(genre);
            }
        }

        Album updatedAlbum = albumRepository.save(album);
        log.info("Updated album with ID: {}", updatedAlbum.getId());

        return albumMapper.toResponse(updatedAlbum);
    }

    @Override
    @Transactional
    public void deleteAlbum(Long id)
    {
        log.debug("Deleting album with ID: {}", id);

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with ID: " + id));

        // Clear genre relationships before deletion
        album.getGenres().clear();
        albumRepository.save(album);

        albumRepository.deleteById(id);
        log.info("Deleted album with ID: {}", id);
    }
}
