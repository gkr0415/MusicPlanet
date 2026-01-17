package com.music.music_inventory_api.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.music_inventory_api.dto.request.CreateArtistRequest;
import com.music.music_inventory_api.dto.request.UpdateArtistRequest;
import com.music.music_inventory_api.entity.Artist;
import com.music.music_inventory_api.repository.ArtistRepository;
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
 * Integration tests for ArtistController.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
public class ArtistControllerIntegrationTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArtistRepository artistRepository;

    @BeforeEach
    public void setUp()
    {
        artistRepository.deleteAll();
    }

    @Test
    public void createArtist_withValidRequest_shouldReturnCreatedArtist() throws Exception
    {
        // Arrange
        CreateArtistRequest request = new CreateArtistRequest();
        request.setName("The Beatles");
        request.setBiography("Legendary British rock band");
        request.setCountry("United Kingdom");

        // Act & Assert
        mockMvc.perform(post("/api/artists").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.name").value("The Beatles"))
                .andExpect(jsonPath("$.biography").value("Legendary British rock band"))
                .andExpect(jsonPath("$.country").value("United Kingdom"));
    }

    @Test
    public void createArtist_withMissingRequiredFields_shouldReturnBadRequest() throws Exception
    {
        // Arrange
        CreateArtistRequest request = new CreateArtistRequest();
        // Missing required name field

        // Act & Assert
        mockMvc.perform(post("/api/artists").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
    }

    @Test
    public void getArtistById_withExistingId_shouldReturnArtist() throws Exception
    {
        // Arrange
        Artist artist = new Artist();
        artist.setName("Pink Floyd");
        artist.setBiography("Progressive rock legends");
        artist.setCountry("United Kingdom");
        artist = artistRepository.save(artist);

        // Act & Assert
        mockMvc.perform(get("/api/artists/{id}", artist.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(artist.getId())).andExpect(jsonPath("$.name").value("Pink Floyd"))
                .andExpect(jsonPath("$.biography").value("Progressive rock legends"));
    }

    @Test
    public void getArtistById_withNonExistentId_shouldReturnNotFound() throws Exception
    {
        // Arrange & Act & Assert
        mockMvc.perform(get("/api/artists/{id}", 9999L)).andExpect(status().isNotFound());
    }

    @Test
    public void getAllArtists_withPagination_shouldReturnPagedArtists() throws Exception
    {
        // Arrange
        Artist artist1 = new Artist();
        artist1.setName("Queen");
        artist1.setBiography("British rock band");
        artist1.setCountry("United Kingdom");
        artistRepository.save(artist1);

        Artist artist2 = new Artist();
        artist2.setName("Led Zeppelin");
        artist2.setBiography("Hard rock pioneers");
        artist2.setCountry("United Kingdom");
        artistRepository.save(artist2);

        // Act & Assert
        mockMvc.perform(get("/api/artists").param("page", "0").param("size", "10")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray()).andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    public void updateArtist_withExistingId_shouldReturnUpdatedArtist() throws Exception
    {
        // Arrange
        Artist artist = new Artist();
        artist.setName("David Bowie");
        artist.setBiography("Original bio");
        artist.setCountry("United Kingdom");
        artist = artistRepository.save(artist);

        UpdateArtistRequest request = new UpdateArtistRequest();
        request.setName("David Bowie");
        request.setBiography("Updated bio - Iconic British musician");
        request.setCountry("United Kingdom");

        // Act & Assert
        mockMvc.perform(put("/api/artists/{id}", artist.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(artist.getId()))
                .andExpect(jsonPath("$.biography").value("Updated bio - Iconic British musician"));
    }

    @Test
    public void updateArtist_withNonExistentId_shouldReturnNotFound() throws Exception
    {
        // Arrange
        UpdateArtistRequest request = new UpdateArtistRequest();
        request.setName("Non-existent Artist");

        // Act & Assert
        mockMvc.perform(put("/api/artists/{id}", 9999L).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isNotFound());
    }

    @Test
    public void deleteArtist_withExistingId_shouldDeleteSuccessfully() throws Exception
    {
        // Arrange
        Artist artist = new Artist();
        artist.setName("Artist to Delete");
        artist.setCountry("Test Country");
        artist = artistRepository.save(artist);

        // Act
        mockMvc.perform(delete("/api/artists/{id}", artist.getId())).andExpect(status().isNoContent());

        // Assert
        mockMvc.perform(get("/api/artists/{id}", artist.getId())).andExpect(status().isNotFound());
    }

    @Test
    public void deleteArtist_withNonExistentId_shouldReturnNotFound() throws Exception
    {
        // Arrange & Act & Assert
        mockMvc.perform(delete("/api/artists/{id}", 9999L)).andExpect(status().isNotFound());
    }

    @Test
    public void getArtistAlbums_withExistingArtist_shouldReturnAlbumsList() throws Exception
    {
        // Arrange
        Artist artist = new Artist();
        artist.setName("Test Artist");
        artist.setCountry("Test Country");
        artist = artistRepository.save(artist);

        // Act & Assert
        // Note: This test assumes albums can be added separately
        // For now, it should return empty list
        mockMvc.perform(get("/api/artists/{id}/albums", artist.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getArtistAlbums_withNonExistentArtist_shouldReturnNotFound() throws Exception
    {
        // Arrange & Act & Assert
        mockMvc.perform(get("/api/artists/{id}/albums", 9999L)).andExpect(status().isNotFound());
    }
}
