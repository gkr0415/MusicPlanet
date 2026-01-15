package com.music.music_inventory_api.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.music.music_inventory_api.dto.request.CreateAlbumRequest;
import com.music.music_inventory_api.dto.response.AlbumResponse;
import com.music.music_inventory_api.entity.Album;
import com.music.music_inventory_api.entity.Artist;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AlbumMapperTest
{

    @Autowired
    private AlbumMapper albumMapper;

    @Test
    void toResponse_withValidAlbum_shouldReturnAlbumResponse()
    {
        // Arrange
        Artist artist = Artist.builder().id(1L).name("The Beatles").build();
        Album album = Album.builder().id(1L).title("Abbey Road").artist(artist).releaseDate(LocalDate.of(1969, 9, 26))
                .price(new BigDecimal("19.99")).stockQuantity(100).build();

        // Act
        AlbumResponse response = albumMapper.toResponse(album);

        // Assert
        assertNotNull(response);
        assertEquals(album.getId(), response.getId());
        assertEquals(album.getTitle(), response.getTitle());
        assertEquals(artist.getId(), response.getArtistId());
        assertEquals(artist.getName(), response.getArtistName());
        assertEquals(album.getPrice(), response.getPrice());
    }

    @Test
    void toEntity_withValidCreateRequest_shouldReturnAlbumEntity()
    {
        // Arrange
        CreateAlbumRequest request = CreateAlbumRequest.builder().title("Dark Side of the Moon").artistId(2L)
                .releaseDate(LocalDate.of(1973, 3, 1)).price(new BigDecimal("24.99")).stockQuantity(50).build();

        // Act
        Album album = albumMapper.toEntity(request);

        // Assert
        assertNotNull(album);
        assertEquals(request.getTitle(), album.getTitle());
        assertEquals(request.getPrice(), album.getPrice());
        assertEquals(request.getStockQuantity(), album.getStockQuantity());
    }

    @Test
    void toResponseList_withValidAlbumList_shouldReturnResponseList()
    {
        // Arrange
        Artist artist = Artist.builder().id(1L).name("Artist").build();
        List<Album> albums = Arrays.asList(
                Album.builder().id(1L).title("Album 1").artist(artist).price(new BigDecimal("10.00")).build(),
                Album.builder().id(2L).title("Album 2").artist(artist).price(new BigDecimal("15.00")).build());

        // Act
        List<AlbumResponse> responses = albumMapper.toResponseList(albums);

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Album 1", responses.get(0).getTitle());
        assertEquals("Album 2", responses.get(1).getTitle());
    }
}
