package com.music.music_inventory_api.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.music_inventory_api.dto.request.CreateAlbumRequest;
import com.music.music_inventory_api.dto.request.UpdateAlbumRequest;
import com.music.music_inventory_api.dto.response.AlbumDetailResponse;
import com.music.music_inventory_api.dto.response.AlbumResponse;
import com.music.music_inventory_api.exception.EntityNotFoundException;
import com.music.music_inventory_api.service.AlbumService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Unit tests for AlbumController.
 */
@WebMvcTest(controllers = AlbumController.class, excludeAutoConfiguration =
{org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@SuppressWarnings("null")
class AlbumControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlbumService albumService;

    private CreateAlbumRequest createRequest;
    private UpdateAlbumRequest updateRequest;
    private AlbumResponse albumResponse;
    private AlbumDetailResponse albumDetailResponse;

    @BeforeEach
    void setUp()
    {
        // Arrange - Set up test data
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
                .price(new BigDecimal("19.99")).stockQuantity(50).build();

        albumDetailResponse = AlbumDetailResponse.builder().id(1L).title("Abbey Road").songs(Collections.emptyList())
                .build();
    }

    @Test
    void createAlbum_withValidRequest_shouldReturnCreated() throws Exception {
        // Arrange
        when(albumService.createAlbum(any(CreateAlbumRequest.class))).thenReturn(albumResponse);

        // Act & Assert
        mockMvc.perform(post("/api/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Abbey Road"))
                .andExpect(jsonPath("$.artistName").value("The Beatles"));

        verify(albumService).createAlbum(any(CreateAlbumRequest.class));
    }

    @Test
    void createAlbum_withInvalidRequest_shouldReturnBadRequest() throws Exception
    {
        // Arrange - Request with missing required fields
        CreateAlbumRequest invalidRequest = new CreateAlbumRequest();

        // Act & Assert
        mockMvc.perform(post("/api/albums").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))).andExpect(status().isBadRequest());

        verify(albumService, never()).createAlbum(any(CreateAlbumRequest.class));
    }

    @Test
    void getAlbumById_withExistingId_shouldReturnAlbum() throws Exception {
        // Arrange
        when(albumService.getAlbumById(1L)).thenReturn(albumDetailResponse);

        // Act & Assert
        mockMvc.perform(get("/api/albums/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Abbey Road"));

        verify(albumService).getAlbumById(1L);
    }

    @Test
    void getAlbumById_withNonExistentId_shouldReturnNotFound() throws Exception {
        // Arrange
        when(albumService.getAlbumById(999L))
                .thenThrow(new EntityNotFoundException("Album not found with ID: 999"));

        // Act & Assert
        mockMvc.perform(get("/api/albums/999"))
                .andExpect(status().isNotFound());

        verify(albumService).getAlbumById(999L);
    }

    @Test
    void getAllAlbums_withDefaultPagination_shouldReturnPageOfAlbums() throws Exception
    {
        // Arrange
        List<AlbumResponse> albums = Collections.singletonList(albumResponse);
        Page<AlbumResponse> page = new PageImpl<>(albums, PageRequest.of(0, 20), 1);
        when(albumService.getAllAlbums(any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/albums")).andExpect(status().isOk()).andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title").value("Abbey Road"))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(albumService).getAllAlbums(any());
    }

    @Test
    void getAllAlbums_withCustomPagination_shouldReturnPageOfAlbums() throws Exception
    {
        // Arrange
        List<AlbumResponse> albums = Collections.singletonList(albumResponse);
        Page<AlbumResponse> page = new PageImpl<>(albums, PageRequest.of(0, 10), 1);
        when(albumService.getAllAlbums(any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/albums").param("page", "0").param("size", "10")).andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(10));

        verify(albumService).getAllAlbums(any());
    }

    @Test
    void searchAlbums_withValidQuery_shouldReturnMatchingAlbums() throws Exception
    {
        // Arrange
        List<AlbumResponse> albums = Collections.singletonList(albumResponse);
        when(albumService.searchAlbums("Beatles")).thenReturn(albums);

        // Act & Assert
        mockMvc.perform(get("/api/albums/search").param("q", "Beatles")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].title").value("Abbey Road"));

        verify(albumService).searchAlbums("Beatles");
    }

    @Test
    void searchAlbums_withEmptyQuery_shouldReturnBadRequest() throws Exception {
        // Arrange
        when(albumService.searchAlbums(anyString()))
                .thenThrow(new IllegalArgumentException("Search term cannot be empty"));

        // Act & Assert
        mockMvc.perform(get("/api/albums/search")
                        .param("q", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAlbumsByGenre_withValidGenreId_shouldReturnAlbums() throws Exception
    {
        // Arrange
        List<AlbumResponse> albums = Collections.singletonList(albumResponse);
        when(albumService.getAlbumsByGenre(1L)).thenReturn(albums);

        // Act & Assert
        mockMvc.perform(get("/api/albums/genre/1")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Abbey Road"));

        verify(albumService).getAlbumsByGenre(1L);
    }

    @Test
    void getAlbumsByGenre_withNonExistentGenreId_shouldReturnNotFound() throws Exception {
        // Arrange
        when(albumService.getAlbumsByGenre(999L))
                .thenThrow(new EntityNotFoundException("Genre not found with ID: 999"));

        // Act & Assert
        mockMvc.perform(get("/api/albums/genre/999"))
                .andExpect(status().isNotFound());

        verify(albumService).getAlbumsByGenre(999L);
    }

    @Test
    void getAlbumsByPriceRange_withValidRange_shouldReturnAlbums() throws Exception
    {
        // Arrange
        List<AlbumResponse> albums = Collections.singletonList(albumResponse);
        when(albumService.getAlbumsByPriceRange(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(albums);

        // Act & Assert
        mockMvc.perform(get("/api/albums/price-range").param("min", "10.00").param("max", "30.00"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Abbey Road"));

        verify(albumService).getAlbumsByPriceRange(any(BigDecimal.class), any(BigDecimal.class));
    }

    @Test
    void getAlbumsByPriceRange_withInvalidRange_shouldReturnBadRequest() throws Exception {
        // Arrange
        when(albumService.getAlbumsByPriceRange(any(BigDecimal.class), any(BigDecimal.class)))
                .thenThrow(new IllegalArgumentException("Minimum price cannot be greater than maximum price"));

        // Act & Assert
        mockMvc.perform(get("/api/albums/price-range")
                        .param("min", "50.00")
                        .param("max", "10.00"))
                .andExpect(status().isBadRequest());

        verify(albumService).getAlbumsByPriceRange(any(BigDecimal.class), any(BigDecimal.class));
    }

    @Test
    void updateAlbum_withValidRequest_shouldReturnUpdatedAlbum() throws Exception
    {
        // Arrange
        AlbumResponse updatedResponse = AlbumResponse.builder().id(1L).title("Abbey Road - Remastered")
                .price(new BigDecimal("24.99")).build();
        when(albumService.updateAlbum(eq(1L), any(UpdateAlbumRequest.class))).thenReturn(updatedResponse);

        // Act & Assert
        mockMvc.perform(put("/api/albums/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.title").value("Abbey Road - Remastered"));

        verify(albumService).updateAlbum(eq(1L), any(UpdateAlbumRequest.class));
    }

    @Test
    void updateAlbum_withNonExistentId_shouldReturnNotFound() throws Exception {
        // Arrange
        when(albumService.updateAlbum(eq(999L), any(UpdateAlbumRequest.class)))
                .thenThrow(new EntityNotFoundException("Album not found with ID: 999"));

        // Act & Assert
        mockMvc.perform(put("/api/albums/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());

        verify(albumService).updateAlbum(eq(999L), any(UpdateAlbumRequest.class));
    }

    @Test
    void deleteAlbum_withExistingId_shouldReturnNoContent() throws Exception
    {
        // Arrange
        doNothing().when(albumService).deleteAlbum(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/albums/1")).andExpect(status().isNoContent());

        verify(albumService).deleteAlbum(1L);
    }

    @Test
    void deleteAlbum_withNonExistentId_shouldReturnNotFound() throws Exception
    {
        // Arrange
        doThrow(new EntityNotFoundException("Album not found with ID: 999")).when(albumService).deleteAlbum(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/albums/999")).andExpect(status().isNotFound());

        verify(albumService).deleteAlbum(999L);
    }
}
