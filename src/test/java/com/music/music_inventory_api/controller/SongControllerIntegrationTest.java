package com.music.music_inventory_api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.music_inventory_api.dto.request.CreateAlbumRequest;
import com.music.music_inventory_api.dto.request.CreateSongRequest;
import com.music.music_inventory_api.dto.request.UpdateSongRequest;
import com.music.music_inventory_api.repository.AlbumRepository;
import com.music.music_inventory_api.repository.ArtistRepository;
import com.music.music_inventory_api.repository.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for SongController.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SongControllerIntegrationTest
{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @BeforeEach
    public void setUp()
    {
        songRepository.deleteAll();
        albumRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    public void createSong_withValidRequest_shouldReturnCreatedSong() throws Exception
    {
        // Arrange
        Long albumId = createTestAlbum("Test Album");

        CreateSongRequest request = new CreateSongRequest();
        request.setTitle("Song Title");
        request.setAlbumId(albumId);
        request.setTrackNumber(1);
        request.setDurationSeconds(180);

        // Act & Assert
        mockMvc.perform(post("/api/songs").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.title").value("Song Title"))
                .andExpect(jsonPath("$.trackNumber").value(1)).andExpect(jsonPath("$.durationSeconds").value(180));
    }

    @Test
    public void createSong_withMissingRequiredFields_shouldReturnBadRequest() throws Exception
    {
        // Arrange
        CreateSongRequest request = new CreateSongRequest();
        request.setTrackNumber(1);

        // Act & Assert
        mockMvc.perform(post("/api/songs").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
    }

    @Test
    public void createSong_withNonExistentAlbum_shouldReturnNotFound() throws Exception
    {
        // Arrange
        CreateSongRequest request = new CreateSongRequest();
        request.setTitle("Song Title");
        request.setAlbumId(999L);
        request.setTrackNumber(1);
        request.setDurationSeconds(180);

        // Act & Assert
        mockMvc.perform(post("/api/songs").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isNotFound());
    }

    @Test
    public void getSongById_withExistingId_shouldReturnSong() throws Exception
    {
        // Arrange
        Long albumId = createTestAlbum("Test Album");
        Long songId = createTestSong("Test Song", albumId, 1, 200);

        // Act & Assert
        mockMvc.perform(get("/api/songs/{id}", songId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(songId)).andExpect(jsonPath("$.title").value("Test Song"))
                .andExpect(jsonPath("$.trackNumber").value(1)).andExpect(jsonPath("$.durationSeconds").value(200));
    }

    @Test
    public void getSongById_withNonExistentId_shouldReturnNotFound() throws Exception
    {
        // Arrange
        Long nonExistentId = 999L;

        // Act & Assert
        mockMvc.perform(get("/api/songs/{id}", nonExistentId)).andExpect(status().isNotFound());
    }

    @Test
    public void getAllSongs_withPagination_shouldReturnPagedSongs() throws Exception
    {
        // Arrange
        Long albumId = createTestAlbum("Test Album");
        createTestSong("Song 1", albumId, 1, 180);
        createTestSong("Song 2", albumId, 2, 200);

        // Act & Assert
        mockMvc.perform(get("/api/songs?page=0&size=10")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray()).andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    public void updateSong_withExistingId_shouldReturnUpdatedSong() throws Exception
    {
        // Arrange
        Long albumId = createTestAlbum("Test Album");
        Long songId = createTestSong("Original Title", albumId, 1, 180);

        UpdateSongRequest updateRequest = UpdateSongRequest.builder().title("Updated Title").trackNumber(2)
                .durationSeconds(220).build();

        // Act & Assert
        mockMvc.perform(put("/api/songs/{id}", songId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(songId)).andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.trackNumber").value(2)).andExpect(jsonPath("$.durationSeconds").value(220));
    }

    @Test
    public void updateSong_withNewAlbumId_shouldUpdateAlbum() throws Exception
    {
        // Arrange
        Long albumId1 = createTestAlbum("Album 1");
        Long albumId2 = createTestAlbum("Album 2");
        Long songId = createTestSong("Test Song", albumId1, 1, 180);

        UpdateSongRequest updateRequest = UpdateSongRequest.builder().albumId(albumId2).build();

        // Act & Assert
        mockMvc.perform(put("/api/songs/{id}", songId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(songId));
    }

    @Test
    public void updateSong_withNonExistentId_shouldReturnNotFound() throws Exception
    {
        // Arrange
        Long nonExistentId = 999L;
        UpdateSongRequest updateRequest = UpdateSongRequest.builder().title("Updated").build();

        // Act & Assert
        mockMvc.perform(put("/api/songs/{id}", nonExistentId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))).andExpect(status().isNotFound());
    }

    @Test
    public void deleteSong_withExistingId_shouldDeleteSuccessfully() throws Exception
    {
        // Arrange
        Long albumId = createTestAlbum("Test Album");
        Long songId = createTestSong("Test Song", albumId, 1, 180);

        // Act & Assert
        mockMvc.perform(delete("/api/songs/{id}", songId)).andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/api/songs/{id}", songId)).andExpect(status().isNotFound());
    }

    @Test
    public void deleteSong_withNonExistentId_shouldReturnNotFound() throws Exception
    {
        // Arrange
        Long nonExistentId = 999L;

        // Act & Assert
        mockMvc.perform(delete("/api/songs/{id}", nonExistentId)).andExpect(status().isNotFound());
    }

    @Test
    public void getSongsByAlbum_withExistingAlbum_shouldReturnSongsList() throws Exception
    {
        // Arrange
        Long albumId = createTestAlbum("Test Album");
        createTestSong("Song 1", albumId, 1, 180);
        createTestSong("Song 2", albumId, 2, 200);

        // Act & Assert
        mockMvc.perform(get("/api/songs/album/{albumId}", albumId)).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void getSongsByAlbum_withNonExistentAlbum_shouldReturnNotFound() throws Exception
    {
        // Arrange
        Long nonExistentAlbumId = 999L;

        // Act & Assert
        mockMvc.perform(get("/api/songs/album/{albumId}", nonExistentAlbumId)).andExpect(status().isNotFound());
    }

    private Long createTestAlbum(String title) throws Exception
    {
        // Create artist using repository directly (persists within @Transactional)
        com.music.music_inventory_api.entity.Artist artist = new com.music.music_inventory_api.entity.Artist();
        artist.setName("Test Artist");
        artist.setCountry("Test Country");
        artist = artistRepository.save(artist);

        // Create album with artist and required fields
        CreateAlbumRequest albumRequest = CreateAlbumRequest.builder().title(title).artistId(artist.getId())
                .price(new java.math.BigDecimal("19.99")).stockQuantity(10).build();

        String response = mockMvc
                .perform(post("/api/albums").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(albumRequest)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    private Long createTestSong(String title, Long albumId, Integer trackNumber, Integer durationSeconds)
            throws Exception
    {
        CreateSongRequest songRequest = CreateSongRequest.builder().title(title).albumId(albumId)
                .trackNumber(trackNumber).durationSeconds(durationSeconds).build();

        String response = mockMvc
                .perform(post("/api/songs").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(songRequest)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }
}
