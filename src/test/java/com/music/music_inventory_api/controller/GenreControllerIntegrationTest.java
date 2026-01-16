package com.music.music_inventory_api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.music_inventory_api.dto.request.CreateGenreRequest;
import com.music.music_inventory_api.dto.request.UpdateGenreRequest;
import com.music.music_inventory_api.repository.GenreRepository;
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
 * Integration tests for GenreController.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class GenreControllerIntegrationTest
{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    public void setUp()
    {
        genreRepository.deleteAll();
    }

    @Test
    public void createGenre_withValidRequest_shouldReturnCreatedGenre() throws Exception
    {
        // Arrange
        CreateGenreRequest request = new CreateGenreRequest();
        request.setName("Rock");
        request.setDescription("Rock music genre");

        // Act & Assert
        mockMvc.perform(post("/api/genres").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.name").value("Rock"))
                .andExpect(jsonPath("$.description").value("Rock music genre"));
    }

    @Test
    public void createGenre_withMissingRequiredFields_shouldReturnBadRequest() throws Exception
    {
        // Arrange
        CreateGenreRequest request = new CreateGenreRequest();
        request.setDescription("Missing name field");

        // Act & Assert
        mockMvc.perform(post("/api/genres").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
    }

    @Test
    public void getGenreById_withExistingId_shouldReturnGenre() throws Exception
    {
        // Arrange
        CreateGenreRequest createRequest = CreateGenreRequest.builder().name("Jazz").description("Jazz music genre")
                .build();

        String createResponse = mockMvc
                .perform(post("/api/genres").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();

        Long genreId = objectMapper.readTree(createResponse).get("id").asLong();

        // Act & Assert
        mockMvc.perform(get("/api/genres/{id}", genreId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(genreId)).andExpect(jsonPath("$.name").value("Jazz"))
                .andExpect(jsonPath("$.description").value("Jazz music genre"));
    }

    @Test
    public void getGenreById_withNonExistentId_shouldReturnNotFound() throws Exception
    {
        // Arrange
        Long nonExistentId = 999L;

        // Act & Assert
        mockMvc.perform(get("/api/genres/{id}", nonExistentId)).andExpect(status().isNotFound());
    }

    @Test
    public void getAllGenres_withPagination_shouldReturnPagedGenres() throws Exception
    {
        // Arrange
        CreateGenreRequest request1 = CreateGenreRequest.builder().name("Rock").description("Rock music").build();
        CreateGenreRequest request2 = CreateGenreRequest.builder().name("Jazz").description("Jazz music").build();

        mockMvc.perform(post("/api/genres").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)));
        mockMvc.perform(post("/api/genres").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)));

        // Act & Assert
        mockMvc.perform(get("/api/genres?page=0&size=10")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray()).andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    public void updateGenre_withExistingId_shouldReturnUpdatedGenre() throws Exception
    {
        // Arrange
        CreateGenreRequest createRequest = CreateGenreRequest.builder().name("Pop").description("Pop music").build();

        String createResponse = mockMvc
                .perform(post("/api/genres").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();

        Long genreId = objectMapper.readTree(createResponse).get("id").asLong();

        UpdateGenreRequest updateRequest = UpdateGenreRequest.builder().name("Pop Music").description("Pop music genre")
                .build();

        // Act & Assert
        mockMvc.perform(put("/api/genres/{id}", genreId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(genreId)).andExpect(jsonPath("$.name").value("Pop Music"))
                .andExpect(jsonPath("$.description").value("Pop music genre"));
    }

    @Test
    public void updateGenre_withNonExistentId_shouldReturnNotFound() throws Exception
    {
        // Arrange
        Long nonExistentId = 999L;
        UpdateGenreRequest updateRequest = UpdateGenreRequest.builder().name("Updated").description("Updated genre")
                .build();

        // Act & Assert
        mockMvc.perform(put("/api/genres/{id}", nonExistentId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))).andExpect(status().isNotFound());
    }

    @Test
    public void deleteGenre_withExistingId_shouldDeleteSuccessfully() throws Exception
    {
        // Arrange
        CreateGenreRequest createRequest = CreateGenreRequest.builder().name("Country").description("Country music")
                .build();

        String createResponse = mockMvc
                .perform(post("/api/genres").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();

        Long genreId = objectMapper.readTree(createResponse).get("id").asLong();

        // Act & Assert
        mockMvc.perform(delete("/api/genres/{id}", genreId)).andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/api/genres/{id}", genreId)).andExpect(status().isNotFound());
    }

    @Test
    public void deleteGenre_withNonExistentId_shouldReturnNotFound() throws Exception
    {
        // Arrange
        Long nonExistentId = 999L;

        // Act & Assert
        mockMvc.perform(delete("/api/genres/{id}", nonExistentId)).andExpect(status().isNotFound());
    }

    @Test
    public void getAlbumsByGenre_withExistingGenre_shouldReturnAlbumsList() throws Exception
    {
        // Arrange
        CreateGenreRequest createRequest = CreateGenreRequest.builder().name("Electronic")
                .description("Electronic music").build();

        String createResponse = mockMvc
                .perform(post("/api/genres").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();

        Long genreId = objectMapper.readTree(createResponse).get("id").asLong();

        // Act & Assert
        mockMvc.perform(get("/api/genres/{id}/albums", genreId)).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getAlbumsByGenre_withNonExistentGenre_shouldReturnNotFound() throws Exception
    {
        // Arrange
        Long nonExistentId = 999L;

        // Act & Assert
        mockMvc.perform(get("/api/genres/{id}/albums", nonExistentId)).andExpect(status().isNotFound());
    }
}
