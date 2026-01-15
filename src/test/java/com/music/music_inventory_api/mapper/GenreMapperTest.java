package com.music.music_inventory_api.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.music.music_inventory_api.dto.request.CreateGenreRequest;
import com.music.music_inventory_api.dto.response.GenreResponse;
import com.music.music_inventory_api.entity.Genre;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GenreMapperTest {

    @Autowired
    private GenreMapper genreMapper;

    @Test
    void toResponse_withValidGenre_shouldReturnGenreResponse() {
        // Arrange
        Genre genre = Genre.builder().id(1L).name("Rock").description("Rock music genre").build();

        // Act
        GenreResponse response = genreMapper.toResponse(genre);

        // Assert
        assertNotNull(response);
        assertEquals(genre.getId(), response.getId());
        assertEquals(genre.getName(), response.getName());
        assertEquals(genre.getDescription(), response.getDescription());
    }

    @Test
    void toEntity_withValidCreateRequest_shouldReturnGenreEntity() {
        // Arrange
        CreateGenreRequest request = CreateGenreRequest.builder().name("Jazz").description("Jazz music genre").build();

        // Act
        Genre genre = genreMapper.toEntity(request);

        // Assert
        assertNotNull(genre);
        assertEquals(request.getName(), genre.getName());
        assertEquals(request.getDescription(), genre.getDescription());
    }

    @Test
    void toResponseSet_withValidGenreSet_shouldReturnResponseSet() {
        // Arrange
        Set<Genre> genres = new HashSet<>(
                Arrays.asList(Genre.builder().id(1L).name("Rock").build(), Genre.builder().id(2L).name("Pop").build()));

        // Act
        Set<GenreResponse> responses = genreMapper.toResponseSet(genres);

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
    }

    @Test
    void toResponseList_withValidGenreList_shouldReturnResponseList() {
        // Arrange
        List<Genre> genres = Arrays.asList(Genre.builder().id(1L).name("Classical").build(),
                Genre.builder().id(2L).name("Electronic").build());

        // Act
        List<GenreResponse> responses = genreMapper.toResponseList(genres);

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
    }
}
