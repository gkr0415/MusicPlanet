package com.music.music_inventory_api.service.impl;

import com.music.music_inventory_api.dto.request.CreateArtistRequest;
import com.music.music_inventory_api.dto.request.UpdateArtistRequest;
import com.music.music_inventory_api.dto.response.AlbumResponse;
import com.music.music_inventory_api.dto.response.ArtistResponse;
import com.music.music_inventory_api.entity.Artist;
import com.music.music_inventory_api.exception.EntityNotFoundException;
import com.music.music_inventory_api.mapper.AlbumMapper;
import com.music.music_inventory_api.mapper.ArtistMapper;
import com.music.music_inventory_api.repository.AlbumRepository;
import com.music.music_inventory_api.repository.ArtistRepository;
import com.music.music_inventory_api.service.ArtistService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of ArtistService interface. Provides business logic for
 * managing artists.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ArtistServiceImpl implements ArtistService
{

    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final ArtistMapper artistMapper;
    private final AlbumMapper albumMapper;

    @Override
    @Transactional
    public ArtistResponse createArtist(CreateArtistRequest request)
    {
        log.info("Creating new artist with name: {}", request.getName());

        Artist artist = artistMapper.toEntity(request);
        Artist savedArtist = artistRepository.save(artist);

        log.info("Successfully created artist with ID: {}", savedArtist.getId());
        return artistMapper.toResponse(savedArtist);
    }

    @Override
    public ArtistResponse getArtistById(Long id)
    {
        log.info("Fetching artist with ID: {}", id);

        Artist artist = artistRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Artist", id));

        return artistMapper.toResponse(artist);
    }

    @Override
    public Page<ArtistResponse> getAllArtists(Pageable pageable)
    {
        log.info("Fetching all artists - Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Artist> artists = artistRepository.findAll(pageable);

        log.info("Found {} artists", artists.getTotalElements());
        return artists.map(artistMapper::toResponse);
    }

    @Override
    @Transactional
    public ArtistResponse updateArtist(Long id, UpdateArtistRequest request)
    {
        log.info("Updating artist with ID: {}", id);

        Artist artist = artistRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Artist", id));

        // Update fields from request
        artistMapper.updateEntityFromRequest(request, artist);

        Artist updatedArtist = artistRepository.save(artist);

        log.info("Successfully updated artist with ID: {}", id);
        return artistMapper.toResponse(updatedArtist);
    }

    @Override
    @Transactional
    public void deleteArtist(Long id)
    {
        log.info("Deleting artist with ID: {}", id);

        if (!artistRepository.existsById(id))
        {
            throw new EntityNotFoundException("Artist", id);
        }

        artistRepository.deleteById(id);
        log.info("Successfully deleted artist with ID: {}", id);
    }

    @Override
    public List<AlbumResponse> getAlbumsByArtist(Long artistId)
    {
        log.info("Fetching albums for artist ID: {}", artistId);

        // Verify artist exists
        if (!artistRepository.existsById(artistId))
        {
            throw new EntityNotFoundException("Artist", artistId);
        }

        List<AlbumResponse> albums = albumRepository.findByArtistId(artistId).stream().map(albumMapper::toResponse)
                .collect(Collectors.toList());

        log.info("Found {} albums for artist ID: {}", albums.size(), artistId);
        return albums;
    }
}
