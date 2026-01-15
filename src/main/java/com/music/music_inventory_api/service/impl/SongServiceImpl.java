package com.music.music_inventory_api.service.impl;

import com.music.music_inventory_api.dto.request.CreateSongRequest;
import com.music.music_inventory_api.dto.request.UpdateSongRequest;
import com.music.music_inventory_api.dto.response.SongResponse;
import com.music.music_inventory_api.entity.Album;
import com.music.music_inventory_api.entity.Song;
import com.music.music_inventory_api.exception.EntityNotFoundException;
import com.music.music_inventory_api.mapper.SongMapper;
import com.music.music_inventory_api.repository.AlbumRepository;
import com.music.music_inventory_api.repository.SongRepository;
import com.music.music_inventory_api.service.SongService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of SongService interface. Provides business logic for managing
 * songs.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final SongMapper songMapper;

    @Override
    @Transactional
    public SongResponse createSong(CreateSongRequest request) {
        log.info("Creating new song with title: {}", request.getTitle());

        // Verify album exists
        Album album = albumRepository.findById(request.getAlbumId())
                .orElseThrow(() -> new EntityNotFoundException("Album", request.getAlbumId()));

        Song song = songMapper.toEntity(request);
        song.setAlbum(album);
        Song savedSong = songRepository.save(song);

        log.info("Successfully created song with ID: {}", savedSong.getId());
        return songMapper.toResponse(savedSong);
    }

    @Override
    public SongResponse getSongById(Long id) {
        log.info("Fetching song with ID: {}", id);

        Song song = songRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Song", id));

        return songMapper.toResponse(song);
    }

    @Override
    public Page<SongResponse> getAllSongs(Pageable pageable) {
        log.info("Fetching all songs - Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Song> songsPage = songRepository.findAll(pageable);

        log.info("Found {} songs", songsPage.getTotalElements());
        return songsPage.map(songMapper::toResponse);
    }

    @Override
    @Transactional
    public SongResponse updateSong(Long id, UpdateSongRequest request) {
        log.info("Updating song with ID: {}", id);

        Song song = songRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Song", id));

        // If albumId is being updated, verify it exists
        if (request.getAlbumId() != null && !request.getAlbumId().equals(song.getAlbum().getId())) {
            Album album = albumRepository.findById(request.getAlbumId())
                    .orElseThrow(() -> new EntityNotFoundException("Album", request.getAlbumId()));
            song.setAlbum(album);
        }

        // Update fields from request
        songMapper.updateEntityFromRequest(request, song);

        Song updatedSong = songRepository.save(song);

        log.info("Successfully updated song with ID: {}", id);
        return songMapper.toResponse(updatedSong);
    }

    @Override
    @Transactional
    public void deleteSong(Long id) {
        log.info("Deleting song with ID: {}", id);

        if (!songRepository.existsById(id)) {
            throw new EntityNotFoundException("Song", id);
        }

        songRepository.deleteById(id);
        log.info("Successfully deleted song with ID: {}", id);
    }

    @Override
    public List<SongResponse> getSongsByAlbum(Long albumId) {
        log.info("Fetching songs for album ID: {}", albumId);

        // Verify album exists
        albumRepository.findById(albumId).orElseThrow(() -> new EntityNotFoundException("Album", albumId));

        List<Song> songs = songRepository.findByAlbumId(albumId);

        log.info("Found {} songs for album ID: {}", songs.size(), albumId);
        return songMapper.toResponseList(songs);
    }
}
