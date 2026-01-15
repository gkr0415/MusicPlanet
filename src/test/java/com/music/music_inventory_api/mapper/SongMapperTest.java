package com.music.music_inventory_api.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.music.music_inventory_api.dto.request.CreateSongRequest;
import com.music.music_inventory_api.dto.response.SongResponse;
import com.music.music_inventory_api.entity.Album;
import com.music.music_inventory_api.entity.Song;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class SongMapperTest
{

    @Autowired
    private SongMapper songMapper;

    @Test
    void toResponse_withValidSong_shouldReturnSongResponse()
    {
        // Arrange
        Album album = Album.builder().id(1L).title("Test Album").build();
        Song song = Song.builder().id(1L).title("Test Song").album(album).trackNumber(1).durationSeconds(180).build();

        // Act
        SongResponse response = songMapper.toResponse(song);

        // Assert
        assertNotNull(response);
        assertEquals(song.getId(), response.getId());
        assertEquals(song.getTitle(), response.getTitle());
        assertEquals(album.getId(), response.getAlbumId());
        assertEquals(song.getTrackNumber(), response.getTrackNumber());
        assertEquals(song.getDurationSeconds(), response.getDurationSeconds());
    }

    @Test
    void toEntity_withValidCreateRequest_shouldReturnSongEntity()
    {
        // Arrange
        CreateSongRequest request = CreateSongRequest.builder().title("New Song").albumId(1L).trackNumber(2)
                .durationSeconds(240).build();

        // Act
        Song song = songMapper.toEntity(request);

        // Assert
        assertNotNull(song);
        assertEquals(request.getTitle(), song.getTitle());
        assertEquals(request.getTrackNumber(), song.getTrackNumber());
        assertEquals(request.getDurationSeconds(), song.getDurationSeconds());
    }

    @Test
    void toResponseList_withValidSongList_shouldReturnResponseList()
    {
        // Arrange
        Album album = Album.builder().id(1L).build();
        List<Song> songs = Arrays.asList(Song.builder().id(1L).title("Song 1").album(album).trackNumber(1).build(),
                Song.builder().id(2L).title("Song 2").album(album).trackNumber(2).build());

        // Act
        List<SongResponse> responses = songMapper.toResponseList(songs);

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Song 1", responses.get(0).getTitle());
        assertEquals("Song 2", responses.get(1).getTitle());
    }
}
