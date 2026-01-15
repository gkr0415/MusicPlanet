package com.music.music_inventory_api.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
import java.math.BigDecimal;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Unit tests for AlbumServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class AlbumServiceImplTest
{

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private SongRepository songRepository;

    @Mock
    private AlbumMapper albumMapper;

    @Mock
    private SongMapper songMapper;

    @InjectMocks
    private AlbumServiceImpl albumService;

    private Artist testArtist;
    private Genre testGenre;
    private Album testAlbum;
    private CreateAlbumRequest createRequest;
    private UpdateAlbumRequest updateRequest;
    private AlbumResponse albumResponse;
    private AlbumDetailResponse albumDetailResponse;

    @BeforeEach
    void setUp()
    {
        // Arrange - Set up test data
        testArtist = new Artist();
        testArtist.setId(1L);
        testArtist.setName("The Beatles");

        testGenre = new Genre();
        testGenre.setId(1L);
        testGenre.setName("Rock");

        testAlbum = new Album();
        testAlbum.setId(1L);
        testAlbum.setTitle("Abbey Road");
        testAlbum.setArtist(testArtist);
        testAlbum.setPrice(new BigDecimal("19.99"));
        testAlbum.setStockQuantity(50);
        testAlbum.setGenres(new HashSet<>(Collections.singletonList(testGenre)));

        createRequest = new CreateAlbumRequest();
        createRequest.setTitle("Abbey Road");
        createRequest.setArtistId(1L);
        createRequest.setPrice(new BigDecimal("19.99"));
        createRequest.setStockQuantity(50);
        createRequest.setGenreIds(new HashSet<>(Collections.singletonList(1L)));

        updateRequest = new UpdateAlbumRequest();
        updateRequest.setTitle("Abbey Road - Remastered");
        updateRequest.setPrice(new BigDecimal("24.99"));

        albumResponse = AlbumResponse.builder().id(1L).title("Abbey Road").artistId(1L).artistName("The Beatles")
                .build();

        albumDetailResponse = AlbumDetailResponse.builder().id(1L).title("Abbey Road").build();
    }

    @Test
    void createAlbum_withValidRequest_shouldCreateAndReturnAlbum() {
        // Arrange
        when(artistRepository.findById(1L)).thenReturn(Optional.of(testArtist));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(testGenre));
        when(albumMapper.toEntity(createRequest)).thenReturn(testAlbum);
        when(albumRepository.save(any(Album.class))).thenReturn(testAlbum);
        when(albumMapper.toResponse(testAlbum)).thenReturn(albumResponse);

        // Act
        AlbumResponse result = albumService.createAlbum(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Abbey Road", result.getTitle());
        verify(artistRepository).findById(1L);
        verify(genreRepository).findById(1L);
        verify(albumRepository).save(any(Album.class));
        verify(albumMapper).toResponse(testAlbum);
    }

    @Test
    void createAlbum_withNonExistentArtist_shouldThrowEntityNotFoundException() {
        // Arrange
        when(artistRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> albumService.createAlbum(createRequest)
        );
        assertTrue(exception.getMessage().contains("Artist not found"));
        verify(albumRepository, never()).save(any(Album.class));
    }

    @Test
    void createAlbum_withNonExistentGenre_shouldThrowEntityNotFoundException() {
        // Arrange
        when(artistRepository.findById(1L)).thenReturn(Optional.of(testArtist));
        when(genreRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> albumService.createAlbum(createRequest)
        );
        assertTrue(exception.getMessage().contains("Genre not found"));
        verify(albumRepository, never()).save(any(Album.class));
    }

    @Test
    void getAlbumById_withExistingId_shouldReturnAlbumDetail()
    {
        // Arrange
        Song testSong = new Song();
        testSong.setId(1L);
        testSong.setTitle("Come Together");

        List<Song> songs = Collections.singletonList(testSong);
        List<SongResponse> songResponses = Collections
                .singletonList(SongResponse.builder().id(1L).title("Come Together").build());

        when(albumRepository.findById(1L)).thenReturn(Optional.of(testAlbum));
        when(songRepository.findByAlbumId(1L)).thenReturn(songs);
        when(songMapper.toResponseList(songs)).thenReturn(songResponses);
        when(albumMapper.toDetailResponse(testAlbum)).thenReturn(albumDetailResponse);

        // Act
        AlbumDetailResponse result = albumService.getAlbumById(1L);

        // Assert
        assertNotNull(result);
        verify(albumRepository).findById(1L);
        verify(songRepository).findByAlbumId(1L);
    }

    @Test
    void getAlbumById_withNonExistentId_shouldThrowEntityNotFoundException() {
        // Arrange
        when(albumRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> albumService.getAlbumById(999L)
        );
        assertTrue(exception.getMessage().contains("Album not found"));
    }

    @Test
    void getAllAlbums_withPageable_shouldReturnPageOfAlbums()
    {
        // Arrange
        List<Album> albums = Collections.singletonList(testAlbum);
        Page<Album> albumPage = new PageImpl<>(albums);
        Pageable pageable = PageRequest.of(0, 20);

        when(albumRepository.findAll(pageable)).thenReturn(albumPage);
        when(albumMapper.toResponse(testAlbum)).thenReturn(albumResponse);

        // Act
        Page<AlbumResponse> result = albumService.getAllAlbums(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(albumRepository).findAll(pageable);
    }

    @Test
    void searchAlbums_withValidSearchTerm_shouldReturnMatchingAlbums()
    {
        // Arrange
        List<Album> albums = Collections.singletonList(testAlbum);
        when(albumRepository.searchByTitleOrArtistName("Beatles")).thenReturn(albums);
        when(albumMapper.toResponseList(albums)).thenReturn(Collections.singletonList(albumResponse));

        // Act
        List<AlbumResponse> result = albumService.searchAlbums("Beatles");

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(albumRepository).searchByTitleOrArtistName("Beatles");
    }

    @Test
    void searchAlbums_withEmptySearchTerm_shouldThrowIllegalArgumentException()
    {
        // Arrange
        String emptySearchTerm = "";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> albumService.searchAlbums(emptySearchTerm));
        assertTrue(exception.getMessage().contains("Search term cannot be empty"));
        verify(albumRepository, never()).searchByTitleOrArtistName(anyString());
    }

    @Test
    void getAlbumsByGenre_withValidGenreId_shouldReturnAlbums()
    {
        // Arrange
        List<Album> albums = Collections.singletonList(testAlbum);
        when(genreRepository.findById(1L)).thenReturn(Optional.of(testGenre));
        when(albumRepository.searchByGenreAndPriceRange(eq("Rock"), any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(albums);
        when(albumMapper.toResponseList(albums)).thenReturn(Collections.singletonList(albumResponse));

        // Act
        List<AlbumResponse> result = albumService.getAlbumsByGenre(1L);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(genreRepository).findById(1L);
    }

    @Test
    void getAlbumsByGenre_withNonExistentGenreId_shouldThrowEntityNotFoundException() {
        // Arrange
        when(genreRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> albumService.getAlbumsByGenre(999L)
        );
        assertTrue(exception.getMessage().contains("Genre not found"));
    }

    @Test
    void getAlbumsByPriceRange_withValidRange_shouldReturnAlbums()
    {
        // Arrange
        BigDecimal minPrice = new BigDecimal("10.00");
        BigDecimal maxPrice = new BigDecimal("30.00");
        List<Album> albums = Collections.singletonList(testAlbum);

        when(albumRepository.findAll()).thenReturn(albums);
        when(albumMapper.toResponseList(anyList())).thenReturn(Collections.singletonList(albumResponse));

        // Act
        List<AlbumResponse> result = albumService.getAlbumsByPriceRange(minPrice, maxPrice);

        // Assert
        assertNotNull(result);
        verify(albumRepository).findAll();
    }

    @Test
    void getAlbumsByPriceRange_withNullPrices_shouldThrowIllegalArgumentException()
    {
        // Arrange
        BigDecimal minPrice = null;
        BigDecimal maxPrice = new BigDecimal("30.00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> albumService.getAlbumsByPriceRange(minPrice, maxPrice));
        assertTrue(exception.getMessage().contains("Price range cannot be null"));
    }

    @Test
    void getAlbumsByPriceRange_withInvertedRange_shouldThrowIllegalArgumentException()
    {
        // Arrange
        BigDecimal minPrice = new BigDecimal("50.00");
        BigDecimal maxPrice = new BigDecimal("10.00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> albumService.getAlbumsByPriceRange(minPrice, maxPrice));
        assertTrue(exception.getMessage().contains("Minimum price cannot be greater than maximum price"));
    }

    @Test
    void updateAlbum_withValidRequest_shouldUpdateAndReturnAlbum() {
        // Arrange
        when(albumRepository.findById(1L)).thenReturn(Optional.of(testAlbum));
        doNothing().when(albumMapper).updateEntityFromRequest(updateRequest, testAlbum);
        when(albumRepository.save(testAlbum)).thenReturn(testAlbum);
        when(albumMapper.toResponse(testAlbum)).thenReturn(albumResponse);

        // Act
        AlbumResponse result = albumService.updateAlbum(1L, updateRequest);

        // Assert
        assertNotNull(result);
        verify(albumRepository).findById(1L);
        verify(albumMapper).updateEntityFromRequest(updateRequest, testAlbum);
        verify(albumRepository).save(testAlbum);
    }

    @Test
    void updateAlbum_withNonExistentId_shouldThrowEntityNotFoundException() {
        // Arrange
        when(albumRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> albumService.updateAlbum(999L, updateRequest)
        );
        assertTrue(exception.getMessage().contains("Album not found"));
        verify(albumRepository, never()).save(any(Album.class));
    }

    @Test
    void deleteAlbum_withExistingId_shouldDeleteAlbum() {
        // Arrange
        when(albumRepository.findById(1L)).thenReturn(Optional.of(testAlbum));
        when(albumRepository.save(testAlbum)).thenReturn(testAlbum);
        doNothing().when(albumRepository).deleteById(1L);

        // Act
        albumService.deleteAlbum(1L);

        // Assert
        verify(albumRepository).findById(1L);
        verify(albumRepository).save(testAlbum);
        verify(albumRepository).deleteById(1L);
    }

    @Test
    void deleteAlbum_withNonExistentId_shouldThrowEntityNotFoundException() {
        // Arrange
        when(albumRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> albumService.deleteAlbum(999L)
        );
        assertTrue(exception.getMessage().contains("Album not found"));
        verify(albumRepository, never()).deleteById(anyLong());
    }
}
