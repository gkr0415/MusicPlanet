package com.music.music_inventory_api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.music_inventory_api.dto.request.CreateOrderItemRequest;
import com.music.music_inventory_api.dto.request.CreateOrderRequest;
import com.music.music_inventory_api.entity.Album;
import com.music.music_inventory_api.entity.Artist;
import com.music.music_inventory_api.entity.Customer;
import com.music.music_inventory_api.entity.Genre;
import com.music.music_inventory_api.entity.Order;
import com.music.music_inventory_api.entity.OrderItem;
import com.music.music_inventory_api.enums.OrderStatus;
import com.music.music_inventory_api.repository.AlbumRepository;
import com.music.music_inventory_api.repository.ArtistRepository;
import com.music.music_inventory_api.repository.CustomerRepository;
import com.music.music_inventory_api.repository.GenreRepository;
import com.music.music_inventory_api.repository.OrderItemRepository;
import com.music.music_inventory_api.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OrderControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private GenreRepository genreRepository;

    private Customer testCustomer;
    private Album testAlbum1;
    private Album testAlbum2;

    @BeforeEach
    void setUp() {
        // Clean up
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        albumRepository.deleteAll();
        artistRepository.deleteAll();
        customerRepository.deleteAll();
        genreRepository.deleteAll();

        // Create test customer
        testCustomer = new Customer();
        testCustomer.setFirstName("John");
        testCustomer.setLastName("Doe");
        testCustomer.setEmail("john.doe@example.com");
        testCustomer.setPhone("123-456-7890");
        testCustomer.setAddress("123 Main St");
        testCustomer.setCity("New York");
        testCustomer.setCountry("USA");
        testCustomer.setPostalCode("10001");
        testCustomer = customerRepository.save(testCustomer);

        // Create test genre
        Genre testGenre = new Genre();
        testGenre.setName("Rock");
        testGenre.setDescription("Rock music");
        testGenre = genreRepository.save(testGenre);

        // Create test artist
        Artist testArtist = new Artist();
        testArtist.setName("Test Artist");
        testArtist.setBiography("Test biography");
        testArtist.setCountry("USA");
        testArtist = artistRepository.save(testArtist);

        // Create test albums
        testAlbum1 = new Album();
        testAlbum1.setTitle("Test Album 1");
        testAlbum1.setArtist(testArtist);
        testAlbum1.getGenres().add(testGenre);
        testAlbum1.setReleaseDate(LocalDate.of(2020, 1, 1));
        testAlbum1.setPrice(new BigDecimal("19.99"));
        testAlbum1.setStockQuantity(10);
        testAlbum1 = albumRepository.save(testAlbum1);

        testAlbum2 = new Album();
        testAlbum2.setTitle("Test Album 2");
        testAlbum2.setArtist(testArtist);
        testAlbum2.getGenres().add(testGenre);
        testAlbum2.setReleaseDate(LocalDate.of(2021, 1, 1));
        testAlbum2.setPrice(new BigDecimal("24.99"));
        testAlbum2.setStockQuantity(5);
        testAlbum2 = albumRepository.save(testAlbum2);
    }

    @Test
    void createOrder_withValidRequest_shouldReturnCreatedOrder() throws Exception {
        // Arrange
        CreateOrderItemRequest item1 = CreateOrderItemRequest.builder().albumId(testAlbum1.getId()).quantity(2).build();

        CreateOrderItemRequest item2 = CreateOrderItemRequest.builder().albumId(testAlbum2.getId()).quantity(1).build();

        CreateOrderRequest request = CreateOrderRequest.builder().customerId(testCustomer.getId())
                .items(Arrays.asList(item1, item2)).build();

        // Act & Assert
        mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId", is(testCustomer.getId().intValue())))
                .andExpect(jsonPath("$.customerName", is("John Doe"))).andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(jsonPath("$.totalAmount", is(64.97))).andExpect(jsonPath("$.items", hasSize(2)));

        // Verify stock was reduced
        Album updatedAlbum1 = albumRepository.findById(testAlbum1.getId()).orElseThrow();
        Album updatedAlbum2 = albumRepository.findById(testAlbum2.getId()).orElseThrow();
        assertThat(updatedAlbum1.getStockQuantity()).isEqualTo(8);
        assertThat(updatedAlbum2.getStockQuantity()).isEqualTo(4);
    }

    @Test
    void createOrder_withNonExistentCustomer_shouldReturnNotFound() throws Exception {
        // Arrange
        CreateOrderItemRequest item = CreateOrderItemRequest.builder().albumId(testAlbum1.getId()).quantity(1).build();

        CreateOrderRequest request = CreateOrderRequest.builder().customerId(999L).items(Arrays.asList(item)).build();

        // Act & Assert
        mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isNotFound());
    }

    @Test
    void createOrder_withNonExistentAlbum_shouldReturnNotFound() throws Exception {
        // Arrange
        CreateOrderItemRequest item = CreateOrderItemRequest.builder().albumId(999L).quantity(1).build();

        CreateOrderRequest request = CreateOrderRequest.builder().customerId(testCustomer.getId())
                .items(Arrays.asList(item)).build();

        // Act & Assert
        mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isNotFound());
    }

    @Test
    void createOrder_withInsufficientStock_shouldReturnBadRequest() throws Exception {
        // Arrange
        CreateOrderItemRequest item = CreateOrderItemRequest.builder().albumId(testAlbum1.getId()).quantity(100)
                .build();

        CreateOrderRequest request = CreateOrderRequest.builder().customerId(testCustomer.getId())
                .items(Arrays.asList(item)).build();

        // Act & Assert
        mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
    }

    @Test
    void getOrderById_withExistingId_shouldReturnOrder() throws Exception {
        // Arrange
        Order order = createTestOrder();

        // Act & Assert
        mockMvc.perform(get("/api/orders/{id}", order.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(order.getId().intValue())))
                .andExpect(jsonPath("$.customerId", is(testCustomer.getId().intValue())))
                .andExpect(jsonPath("$.customerName", is("John Doe"))).andExpect(jsonPath("$.status", is("PENDING")));
    }

    @Test
    void getOrderById_withNonExistentId_shouldReturnNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/orders/{id}", 999L)).andExpect(status().isNotFound());
    }

    @Test
    void getOrdersByCustomer_shouldReturnCustomerOrders() throws Exception {
        // Arrange
        createTestOrder();
        createTestOrder();

        // Act & Assert
        mockMvc.perform(get("/api/orders/customer/{customerId}", testCustomer.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].customerId", is(testCustomer.getId().intValue())))
                .andExpect(jsonPath("$[1].customerId", is(testCustomer.getId().intValue())));
    }

    @Test
    void getOrdersByCustomer_withNoOrders_shouldReturnEmptyList() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/orders/customer/{customerId}", testCustomer.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void updateOrderStatus_withValidStatus_shouldUpdateOrder() throws Exception {
        // Arrange
        Order order = createTestOrder();

        // Act & Assert
        mockMvc.perform(put("/api/orders/{id}/status", order.getId()).param("status", "PROCESSING"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(order.getId().intValue())))
                .andExpect(jsonPath("$.status", is("PROCESSING")));
    }

    @Test
    void updateOrderStatus_withCancelledOrder_shouldReturnBadRequest() throws Exception {
        // Arrange
        Order order = createTestOrder();
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        // Act & Assert
        mockMvc.perform(put("/api/orders/{id}/status", order.getId()).param("status", "PROCESSING"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cancelOrder_withPendingOrder_shouldCancelAndRestoreStock() throws Exception {
        // Arrange
        Order order = createTestOrder();
        int initialStock = testAlbum1.getStockQuantity();

        // Act & Assert
        mockMvc.perform(post("/api/orders/{id}/cancel", order.getId())).andExpect(status().isNoContent());

        // Verify order was cancelled
        Order cancelledOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(cancelledOrder.getStatus()).isEqualTo(OrderStatus.CANCELLED);

        // Verify stock was restored
        Album updatedAlbum = albumRepository.findById(testAlbum1.getId()).orElseThrow();
        assertThat(updatedAlbum.getStockQuantity()).isEqualTo(initialStock + 2);
    }

    @Test
    void cancelOrder_withShippedOrder_shouldReturnBadRequest() throws Exception {
        // Arrange
        Order order = createTestOrder();
        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);

        // Act & Assert
        mockMvc.perform(post("/api/orders/{id}/cancel", order.getId())).andExpect(status().isBadRequest());
    }

    @Test
    void cancelOrder_withNonExistentOrder_shouldReturnNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/orders/{id}/cancel", 999L)).andExpect(status().isNotFound());
    }

    private Order createTestOrder() {
        Order order = new Order();
        order.setCustomer(testCustomer);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(new BigDecimal("39.98"));
        order.setShippingAddress("123 Main St");
        order.setShippingCity("New York");
        order.setShippingCountry("USA");
        order.setShippingPostalCode("10001");
        order = orderRepository.save(order);

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setAlbum(testAlbum1);
        item.setQuantity(2);
        item.setUnitPrice(testAlbum1.getPrice());
        item.setSubtotal(testAlbum1.getPrice().multiply(new BigDecimal(2)));
        orderItemRepository.save(item);

        // Add item to order's collection so it can be accessed when cancelling
        order.getOrderItems().add(item);

        // Reduce stock to simulate order creation
        testAlbum1.setStockQuantity(testAlbum1.getStockQuantity() - 2);
        albumRepository.save(testAlbum1);

        return order;
    }
}
