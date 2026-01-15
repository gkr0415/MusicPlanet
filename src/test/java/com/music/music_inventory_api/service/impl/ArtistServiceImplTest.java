package com.music.music_inventory_api.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.music.music_inventory_api.dto.request.CreateArtistRequest;
import com.music.music_inventory_api.dto.request.UpdateArtistRequest;
import com.music.music_inventory_api.dto.response.AlbumResponse;
import com.music.music_inventory_api.dto.response.ArtistResponse;
import com.music.music_inventory_api.entity.Album;
import com.music.music_inventory_api.entity.Artist;
import com.music.music_inventory_api.exception.EntityNotFoundException;
import com.music.music_inventory_api.mapper.AlbumMapper;
import com.music.music_inventory_api.mapper.ArtistMapper;
import com.music.music_inventory_api.repository.AlbumRepository;
import com.music.music_inventory_api.repository.ArtistRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
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
@DisplayName("ArtistService Unit Tests")
class ArtistServiceImplTest
{

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ArtistMapper artistMapper;

    @Mock
    private AlbumMapper albumMapper;

    @InjectMocks
    private ArtistServiceImpl artistService;

    private Artist artist;
    private ArtistResponse artistResponse;
    private CreateArtistRequest createRequest;
    private UpdateArtistRequest updateRequest;
    private Album album;
    private AlbumResponse albumResponse;

    @BeforeEach
    void setUp()
    {
        // Set up test data
        artist = new Artist();
        artist.setId(1L);
        artist.setName("The Beatles");
        artist.setCountry("United Kingdom");
        artist.setBiography("Legendary rock band");

        artistResponse = new ArtistResponse();
        artistResponse.setId(1L);
        artistResponse.setName("The Beatles");
        artistResponse.setCountry("United Kingdom");
        artistResponse.setBiography("Legendary rock band");

        createRequest = new CreateArtistRequest();
        createRequest.setName("The Beatles");
        createRequest.setCountry("United Kingdom");
        createRequest.setBiography("Legendary rock band");

        updateRequest = new UpdateArtistRequest();
        updateRequest.setName("The Beatles Updated");
        updateRequest.setCountry("UK");

        album = new Album();
        album.setId(1L);
        album.setTitle("Abbey Road");
        album.setPrice(BigDecimal.valueOf(19.99));
        album.setReleaseDate(LocalDate.of(1969, 9, 26));
        album.setArtist(artist);

        albumResponse = new AlbumResponse();
        albumResponse.setId(1L);
        albumResponse.setTitle("Abbey Road");
        albumResponse.setPrice(BigDecimal.valueOf(19.99));
        albumResponse.setReleaseDate(LocalDate.of(1969, 9, 26));
    }

    @Test
    @DisplayName("Should create artist successfully")
    void createArtist_withValidRequest_shouldReturnArtistResponse() {
        // Arrange
        when(artistMapper.toEntity(createRequest)).thenReturn(artist);
        when(artistRepository.save(artist)).thenReturn(artist);
        when(artistMapper.toResponse(artist)).thenReturn(artistResponse);

        // Act
        ArtistResponse result = artistService.createArtist(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals(artistResponse.getId(), result.getId());
        assertEquals(artistResponse.getName(), result.getName());

        verify(artistMapper).toEntity(createRequest);
        verify(artistRepository).save(artist);
        verify(artistMapper).toResponse(artist);
    }

    @Test
    @DisplayName("Should get artist by ID successfully")
    void getArtistById_withExistingId_shouldReturnArtistResponse() {
        // Arrange
        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));
        when(artistMapper.toResponse(artist)).thenReturn(artistResponse);

        // Act
        ArtistResponse result = artistService.getArtistById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(artistResponse.getId(), result.getId());
        assertEquals(artistResponse.getName(), result.getName());

        verify(artistRepository).findById(1L);
        verify(artistMapper).toResponse(artist);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when artist not found")
    void getArtistById_withNonExistentId_shouldThrowEntityNotFoundException() {
        // Arrange
        when(artistRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> artistService.getArtistById(999L));

        assertTrue(exception.getMessage().contains("Artist"));
        assertTrue(exception.getMessage().contains("999"));

        verify(artistRepository).findById(999L);
        verify(artistMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Should get all artists with pagination")
    void getAllArtists_withPageable_shouldReturnPagedArtists()
    {
        // Arrange
        Artist artist2 = new Artist();
        artist2.setId(2L);
        artist2.setName("Pink Floyd");

        List<Artist> artists = Arrays.asList(artist, artist2);
        Page<Artist> artistPage = new PageImpl<>(artists);
        Pageable pageable = PageRequest.of(0, 10);

        ArtistResponse response2 = new ArtistResponse();
        response2.setId(2L);
        response2.setName("Pink Floyd");

        when(artistRepository.findAll(pageable)).thenReturn(artistPage);
        when(artistMapper.toResponse(artist)).thenReturn(artistResponse);
        when(artistMapper.toResponse(artist2)).thenReturn(response2);

        // Act
        Page<ArtistResponse> result = artistService.getAllArtists(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(artistResponse.getName(), result.getContent().get(0).getName());
        assertEquals(response2.getName(), result.getContent().get(1).getName());

        verify(artistRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should update artist successfully")
    void updateArtist_withExistingId_shouldReturnUpdatedArtist() {
        // Arrange
        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));
        doNothing().when(artistMapper).updateEntityFromRequest(updateRequest, artist);
        when(artistRepository.save(artist)).thenReturn(artist);
        when(artistMapper.toResponse(artist)).thenReturn(artistResponse);

        // Act
        ArtistResponse result = artistService.updateArtist(1L, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(artistResponse.getId(), result.getId());

        verify(artistRepository).findById(1L);
        verify(artistMapper).updateEntityFromRequest(updateRequest, artist);
        verify(artistRepository).save(artist);
        verify(artistMapper).toResponse(artist);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating non-existent artist")
    void updateArtist_withNonExistentId_shouldThrowEntityNotFoundException() {
        // Arrange
        when(artistRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> artistService.updateArtist(999L, updateRequest));

        assertTrue(exception.getMessage().contains("Artist"));
        assertTrue(exception.getMessage().contains("999"));

        verify(artistRepository).findById(999L);
        verify(artistRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete artist successfully")
    void deleteArtist_withExistingId_shouldDeleteSuccessfully() {
        // Arrange
        when(artistRepository.existsById(1L)).thenReturn(true);
        doNothing().when(artistRepository).deleteById(1L);

        // Act
        artistService.deleteArtist(1L);

        // Assert
        verify(artistRepository).existsById(1L);
        verify(artistRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when deleting non-existent artist")
    void deleteArtist_withNonExistentId_shouldThrowEntityNotFoundException() {
        // Arrange
        when(artistRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> artistService.deleteArtist(999L));

        assertTrue(exception.getMessage().contains("Artist"));
        assertTrue(exception.getMessage().contains("999"));

        verify(artistRepository).existsById(999L);
        verify(artistRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should get albums by artist successfully")
    void getAlbumsByArtist_withExistingArtist_shouldReturnAlbums()
    {
        // Arrange
        Album album2 = new Album();
        album2.setId(2L);
        album2.setTitle("Let It Be");
        album2.setArtist(artist);

        AlbumResponse albumResponse2 = new AlbumResponse();
        albumResponse2.setId(2L);
        albumResponse2.setTitle("Let It Be");

        List<Album> albums = Arrays.asList(album, album2);

        when(artistRepository.existsById(1L)).thenReturn(true);
        when(albumRepository.findByArtistId(1L)).thenReturn(albums);
        when(albumMapper.toResponse(album)).thenReturn(albumResponse);
        when(albumMapper.toResponse(album2)).thenReturn(albumResponse2);

        // Act
        List<AlbumResponse> result = artistService.getAlbumsByArtist(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(albumResponse.getTitle(), result.get(0).getTitle());
        assertEquals(albumResponse2.getTitle(), result.get(1).getTitle());

        verify(artistRepository).existsById(1L);
        verify(albumRepository).findByArtistId(1L);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when getting albums for non-existent artist")
    void getAlbumsByArtist_withNonExistentArtist_shouldThrowEntityNotFoundException() {
        // Arrange
        when(artistRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> artistService.getAlbumsByArtist(999L));

        assertTrue(exception.getMessage().contains("Artist"));
        assertTrue(exception.getMessage().contains("999"));

        verify(artistRepository).existsById(999L);
        verify(albumRepository, never()).findByArtistId(any());
    }

    @Test
    @DisplayName("Should return empty list when artist has no albums")
    void getAlbumsByArtist_withArtistHavingNoAlbums_shouldReturnEmptyList() {
        // Arrange
        when(artistRepository.existsById(1L)).thenReturn(true);
        when(albumRepository.findByArtistId(1L)).thenReturn(Arrays.asList());

        // Act
        List<AlbumResponse> result = artistService.getAlbumsByArtist(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(artistRepository).existsById(1L);
        verify(albumRepository).findByArtistId(1L);
    }
}
