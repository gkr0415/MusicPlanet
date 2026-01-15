package com.music.music_inventory_api.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.music.music_inventory_api.dto.request.CreateArtistRequest;
import com.music.music_inventory_api.dto.request.UpdateArtistRequest;
import com.music.music_inventory_api.dto.response.ArtistResponse;
import com.music.music_inventory_api.entity.Artist;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ArtistMapperTest
{

    @Autowired
    private ArtistMapper artistMapper;

    @Test
    void shouldMapArtistToResponse()
    {
        Artist artist = Artist.builder().id(1L).name("The Beatles").country("UK").biography("Legendary rock band")
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        ArtistResponse response = artistMapper.toResponse(artist);

        assertNotNull(response);
        assertEquals(artist.getId(), response.getId());
        assertEquals(artist.getName(), response.getName());
        assertEquals(artist.getCountry(), response.getCountry());
        assertEquals(artist.getBiography(), response.getBiography());
    }

    @Test
    void shouldMapCreateRequestToEntity()
    {
        CreateArtistRequest request = CreateArtistRequest.builder().name("Pink Floyd").country("UK")
                .biography("Progressive rock pioneers").build();

        Artist artist = artistMapper.toEntity(request);

        assertNotNull(artist);
        assertEquals(request.getName(), artist.getName());
        assertEquals(request.getCountry(), artist.getCountry());
        assertEquals(request.getBiography(), artist.getBiography());
    }

    @Test
    void shouldUpdateEntityFromRequest()
    {
        Artist artist = Artist.builder().id(1L).name("Old Name").country("Old Country").biography("Old Bio").build();

        UpdateArtistRequest request = UpdateArtistRequest.builder().name("New Name").country("New Country")
                .biography("New Bio").build();

        artistMapper.updateEntityFromRequest(request, artist);

        assertEquals(request.getName(), artist.getName());
        assertEquals(request.getCountry(), artist.getCountry());
        assertEquals(request.getBiography(), artist.getBiography());
        assertEquals(1L, artist.getId());
    }

    @Test
    void shouldMapArtistListToResponseList()
    {
        List<Artist> artists = Arrays.asList(Artist.builder().id(1L).name("Artist 1").country("US").build(),
                Artist.builder().id(2L).name("Artist 2").country("UK").build());

        List<ArtistResponse> responses = artistMapper.toResponseList(artists);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Artist 1", responses.get(0).getName());
        assertEquals("Artist 2", responses.get(1).getName());
    }
}
