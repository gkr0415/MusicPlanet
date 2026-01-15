package com.music.music_inventory_api.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.music.music_inventory_api.dto.request.CreateSongRequest;
import com.music.music_inventory_api.dto.request.UpdateSongRequest;
import com.music.music_inventory_api.dto.response.SongResponse;
import com.music.music_inventory_api.entity.Album;
import com.music.music_inventory_api.entity.Song;
import com.music.music_inventory_api.exception.EntityNotFoundException;
import com.music.music_inventory_api.mapper.SongMapper;
import com.music.music_inventory_api.repository.AlbumRepository;
import com.music.music_inventory_api.repository.SongRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@DisplayName("SongService Unit Tests")
class SongServiceImplTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private SongMapper songMapper;

    @InjectMocks
    private SongServiceImpl songService;

    private Song song;
    private SongResponse songResponse;
    private CreateSongRequest createRequest;
    private UpdateSongRequest updateRequest;
    private Album album;

    @BeforeEach
    void setUp() {
        album = Album.builder().id(1L).title("Abbey Road").build();

        song = Song.builder().id(1L).title("Come Together").album(album).trackNumber(1).durationSeconds(259).build();

        songResponse = SongResponse.builder().id(1L).title("Come Together").albumId(1L).trackNumber(1)
                .durationSeconds(259).build();

        createRequest = CreateSongRequest.builder().title("Come Together").albumId(1L).trackNumber(1)
                .durationSeconds(259).build();

        updateRequest = UpdateSongRequest.builder().title("Come Together (Remastered)").trackNumber(1)
                .durationSeconds(265).build();
    }

    @Test
    @DisplayName("Should create song successfully")
    void createSong_withValidRequest_shouldReturnSongResponse() {
        // Arrange
        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
        when(songMapper.toEntity(createRequest)).thenReturn(song);
        when(songRepository.save(any(Song.class))).thenReturn(song);
        when(songMapper.toResponse(song)).thenReturn(songResponse);

        // Act
        SongResponse result = songService.createSong(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals(songResponse.getId(), result.getId());
        assertEquals(songResponse.getTitle(), result.getTitle());
        verify(albumRepository).findById(1L);
        verify(songRepository).save(any(Song.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when album not found during create")
    void createSong_withNonExistentAlbum_shouldThrowEntityNotFoundException() {
        // Arrange
        when(albumRepository.findById(999L)).thenReturn(Optional.empty());
        CreateSongRequest invalidRequest = CreateSongRequest.builder().title("Test Song").albumId(999L)
                .trackNumber(1).build();

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> songService.createSong(invalidRequest));
        verify(albumRepository).findById(999L);
        verify(songRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get song by ID successfully")
    void getSongById_withExistingId_shouldReturnSongResponse() {
        // Arrange
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        when(songMapper.toResponse(song)).thenReturn(songResponse);

        // Act
        SongResponse result = songService.getSongById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(songResponse.getId(), result.getId());
        assertEquals(songResponse.getTitle(), result.getTitle());
        verify(songRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when song not found")
    void getSongById_withNonExistentId_shouldThrowEntityNotFoundException() {
        // Arrange
        when(songRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> songService.getSongById(999L));
        verify(songRepository).findById(999L);
    }

    @Test
    @DisplayName("Should get all songs successfully")
    void getAllSongs_withPageable_shouldReturnPagedSongs() {
        // Arrange
        Song song2 = Song.builder().id(2L).title("Something").album(album).trackNumber(2).durationSeconds(182).build();
        List<Song> songs = Arrays.asList(song, song2);
        Page<Song> songPage = new PageImpl<>(songs);
        Pageable pageable = PageRequest.of(0, 10);

        when(songRepository.findAll(pageable)).thenReturn(songPage);
        when(songMapper.toResponse(any(Song.class))).thenReturn(songResponse);

        // Act
        Page<SongResponse> result = songService.getAllSongs(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(songRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should update song successfully")
    void updateSong_withExistingId_shouldReturnUpdatedSong() {
        // Arrange
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        when(songRepository.save(song)).thenReturn(song);
        when(songMapper.toResponse(song)).thenReturn(songResponse);

        // Act
        SongResponse result = songService.updateSong(1L, updateRequest);

        // Assert
        assertNotNull(result);
        verify(songRepository).findById(1L);
        verify(songMapper).updateEntityFromRequest(updateRequest, song);
        verify(songRepository).save(song);
    }

    @Test
    @DisplayName("Should update song with new album")
    void updateSong_withNewAlbumId_shouldUpdateAlbum() {
        // Arrange
        Album newAlbum = Album.builder().id(2L).title("Let It Be").build();
        UpdateSongRequest requestWithNewAlbum = UpdateSongRequest.builder().albumId(2L).build();

        when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        when(albumRepository.findById(2L)).thenReturn(Optional.of(newAlbum));
        when(songRepository.save(song)).thenReturn(song);
        when(songMapper.toResponse(song)).thenReturn(songResponse);

        // Act
        SongResponse result = songService.updateSong(1L, requestWithNewAlbum);

        // Assert
        assertNotNull(result);
        verify(albumRepository).findById(2L);
        verify(songRepository).save(song);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating non-existent song")
    void updateSong_withNonExistentId_shouldThrowEntityNotFoundException() {
        // Arrange
        when(songRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> songService.updateSong(999L, updateRequest));
        verify(songRepository).findById(999L);
        verify(songRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete song successfully")
    void deleteSong_withExistingId_shouldDeleteSuccessfully() {
        // Arrange
        when(songRepository.existsById(1L)).thenReturn(true);

        // Act
        songService.deleteSong(1L);

        // Assert
        verify(songRepository).existsById(1L);
        verify(songRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when deleting non-existent song")
    void deleteSong_withNonExistentId_shouldThrowEntityNotFoundException() {
        // Arrange
        when(songRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> songService.deleteSong(999L));
        verify(songRepository).existsById(999L);
        verify(songRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should get songs by album successfully")
    void getSongsByAlbum_withExistingAlbum_shouldReturnSongs() {
        // Arrange
        Song song2 = Song.builder().id(2L).title("Something").album(album).trackNumber(2).durationSeconds(182).build();
        List<Song> songs = Arrays.asList(song, song2);

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
        when(songRepository.findByAlbumId(1L)).thenReturn(songs);
        when(songMapper.toResponseList(songs))
                .thenReturn(Arrays.asList(songResponse, SongResponse.builder().id(2L).title("Something").build()));

        // Act
        List<SongResponse> result = songService.getSongsByAlbum(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(albumRepository).findById(1L);
        verify(songRepository).findByAlbumId(1L);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when getting songs for non-existent album")
    void getSongsByAlbum_withNonExistentAlbum_shouldThrowEntityNotFoundException() {
        // Arrange
        when(albumRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> songService.getSongsByAlbum(999L));
        verify(albumRepository).findById(999L);
    }

    @Test
    @DisplayName("Should return empty list when album has no songs")
    void getSongsByAlbum_withAlbumHavingNoSongs_shouldReturnEmptyList() {
        // Arrange
        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
        when(songRepository.findByAlbumId(1L)).thenReturn(Arrays.asList());
        when(songMapper.toResponseList(anyList())).thenReturn(Arrays.asList());

        // Act
        List<SongResponse> result = songService.getSongsByAlbum(1L);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(albumRepository).findById(1L);
        verify(songRepository).findByAlbumId(1L);
    }
}
