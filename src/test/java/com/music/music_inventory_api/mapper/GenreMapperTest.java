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
class GenreMapperTest
{

    @Autowired
    private GenreMapper genreMapper;

    @Test
    void shouldMapGenreToResponse()
    {
        Genre genre = Genre.builder().id(1L).name("Rock").description("Rock music genre").build();

        GenreResponse response = genreMapper.toResponse(genre);

        assertNotNull(response);
        assertEquals(genre.getId(), response.getId());
        assertEquals(genre.getName(), response.getName());
        assertEquals(genre.getDescription(), response.getDescription());
    }

    @Test
    void shouldMapCreateRequestToEntity()
    {
        CreateGenreRequest request = CreateGenreRequest.builder().name("Jazz").description("Jazz music genre").build();

        Genre genre = genreMapper.toEntity(request);

        assertNotNull(genre);
        assertEquals(request.getName(), genre.getName());
        assertEquals(request.getDescription(), genre.getDescription());
    }

    @Test
    void shouldMapGenreSetToResponseSet()
    {
        Set<Genre> genres = new HashSet<>(
                Arrays.asList(Genre.builder().id(1L).name("Rock").build(), Genre.builder().id(2L).name("Pop").build()));

        Set<GenreResponse> responses = genreMapper.toResponseSet(genres);

        assertNotNull(responses);
        assertEquals(2, responses.size());
    }

    @Test
    void shouldMapGenreListToResponseList()
    {
        List<Genre> genres = Arrays.asList(Genre.builder().id(1L).name("Classical").build(),
                Genre.builder().id(2L).name("Electronic").build());

        List<GenreResponse> responses = genreMapper.toResponseList(genres);

        assertNotNull(responses);
        assertEquals(2, responses.size());
    }
}
