package com.music.music_inventory_api.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
@DisplayName("GenreService Unit Tests")
class GenreServiceImplTest {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private GenreMapper genreMapper;

    @Mock
    private AlbumMapper albumMapper;

    @InjectMocks
    private GenreServiceImpl genreService;

    private Genre genre;
    private GenreResponse genreResponse;
    private CreateGenreRequest createRequest;
    private UpdateGenreRequest updateRequest;
    private Album album;
    private AlbumResponse albumResponse;

    @BeforeEach
    void setUp() {
        genre = Genre.builder().id(1L).name("Rock").description("Rock music genre").build();

        genreResponse = GenreResponse.builder().id(1L).name("Rock").description("Rock music genre").build();

        createRequest = CreateGenreRequest.builder().name("Rock").description("Rock music genre").build();

        updateRequest = UpdateGenreRequest.builder().name("Progressive Rock")
                .description("Progressive rock music genre").build();

        album = Album.builder().id(1L).title("Dark Side of the Moon").build();
        Set<Genre> genres = new HashSet<>();
        genres.add(genre);
        album.setGenres(genres);

        albumResponse = AlbumResponse.builder().id(1L).title("Dark Side of the Moon").build();
    }

    @Test
    @DisplayName("Should create genre successfully")
    void createGenre_withValidRequest_shouldReturnGenreResponse() {
        // Arrange
        when(genreMapper.toEntity(createRequest)).thenReturn(genre);
        when(genreRepository.save(genre)).thenReturn(genre);
        when(genreMapper.toResponse(genre)).thenReturn(genreResponse);

        // Act
        GenreResponse result = genreService.createGenre(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals(genreResponse.getId(), result.getId());
        assertEquals(genreResponse.getName(), result.getName());
        verify(genreRepository).save(genre);
    }

    @Test
    @DisplayName("Should get genre by ID successfully")
    void getGenreById_withExistingId_shouldReturnGenreResponse() {
        // Arrange
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(genreMapper.toResponse(genre)).thenReturn(genreResponse);

        // Act
        GenreResponse result = genreService.getGenreById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(genreResponse.getId(), result.getId());
        assertEquals(genreResponse.getName(), result.getName());
        verify(genreRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when genre not found")
    void getGenreById_withNonExistentId_shouldThrowEntityNotFoundException() {
        // Arrange
        when(genreRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> genreService.getGenreById(999L));
        verify(genreRepository).findById(999L);
    }

    @Test
    @DisplayName("Should get all genres successfully")
    void getAllGenres_withPageable_shouldReturnPagedGenres() {
        // Arrange
        Genre genre2 = Genre.builder().id(2L).name("Jazz").description("Jazz music genre").build();
        List<Genre> genres = Arrays.asList(genre, genre2);
        Page<Genre> genrePage = new PageImpl<>(genres);
        Pageable pageable = PageRequest.of(0, 10);

        when(genreRepository.findAll(pageable)).thenReturn(genrePage);
        when(genreMapper.toResponse(any(Genre.class))).thenReturn(genreResponse);

        // Act
        Page<GenreResponse> result = genreService.getAllGenres(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(genreRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should update genre successfully")
    void updateGenre_withExistingId_shouldReturnUpdatedGenre() {
        // Arrange
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(genreRepository.save(genre)).thenReturn(genre);
        when(genreMapper.toResponse(genre)).thenReturn(genreResponse);

        // Act
        GenreResponse result = genreService.updateGenre(1L, updateRequest);

        // Assert
        assertNotNull(result);
        verify(genreRepository).findById(1L);
        verify(genreMapper).updateEntityFromRequest(updateRequest, genre);
        verify(genreRepository).save(genre);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating non-existent genre")
    void updateGenre_withNonExistentId_shouldThrowEntityNotFoundException() {
        // Arrange
        when(genreRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> genreService.updateGenre(999L, updateRequest));
        verify(genreRepository).findById(999L);
        verify(genreRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete genre successfully")
    void deleteGenre_withExistingId_shouldDeleteSuccessfully() {
        // Arrange
        when(genreRepository.existsById(1L)).thenReturn(true);

        // Act
        genreService.deleteGenre(1L);

        // Assert
        verify(genreRepository).existsById(1L);
        verify(genreRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when deleting non-existent genre")
    void deleteGenre_withNonExistentId_shouldThrowEntityNotFoundException() {
        // Arrange
        when(genreRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> genreService.deleteGenre(999L));
        verify(genreRepository).existsById(999L);
        verify(genreRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should get albums by genre successfully")
    void getAlbumsByGenre_withExistingGenre_shouldReturnAlbums() {
        // Arrange
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(albumRepository.findAll()).thenReturn(Arrays.asList(album));
        when(albumMapper.toResponseList(anyList())).thenReturn(Arrays.asList(albumResponse));

        // Act
        List<AlbumResponse> result = genreService.getAlbumsByGenre(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(genreRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when getting albums for non-existent genre")
    void getAlbumsByGenre_withNonExistentGenre_shouldThrowEntityNotFoundException() {
        // Arrange
        when(genreRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> genreService.getAlbumsByGenre(999L));
        verify(genreRepository).findById(999L);
    }

    @Test
    @DisplayName("Should return empty list when genre has no albums")
    void getAlbumsByGenre_withGenreHavingNoAlbums_shouldReturnEmptyList() {
        // Arrange
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(albumRepository.findAll()).thenReturn(new ArrayList<>());
        when(albumMapper.toResponseList(anyList())).thenReturn(new ArrayList<>());

        // Act
        List<AlbumResponse> result = genreService.getAlbumsByGenre(1L);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(genreRepository).findById(1L);
    }
}
