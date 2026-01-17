package com.music.music_inventory_api.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.music.music_inventory_api.dto.request.CreateOrderItemRequest;
import com.music.music_inventory_api.dto.request.CreateOrderRequest;
import com.music.music_inventory_api.dto.response.OrderResponse;
import com.music.music_inventory_api.entity.Album;
import com.music.music_inventory_api.entity.Artist;
import com.music.music_inventory_api.entity.Customer;
import com.music.music_inventory_api.entity.Order;
import com.music.music_inventory_api.entity.OrderItem;
import com.music.music_inventory_api.enums.OrderStatus;
import com.music.music_inventory_api.exception.EntityNotFoundException;
import com.music.music_inventory_api.mapper.OrderMapper;
import com.music.music_inventory_api.repository.AlbumRepository;
import com.music.music_inventory_api.repository.CustomerRepository;
import com.music.music_inventory_api.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Unit tests for OrderServiceImpl. */
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest
{

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Customer testCustomer;
    private Album testAlbum;
    private Order testOrder;
    private OrderResponse testOrderResponse;

    @BeforeEach
    void setUp()
    {
        // Create test customer
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setFirstName("John");
        testCustomer.setLastName("Doe");
        testCustomer.setEmail("john@example.com");
        testCustomer.setPassword("password123");

        // Create test artist
        Artist testArtist = new Artist();
        testArtist.setId(1L);
        testArtist.setName("Test Artist");

        // Create test album
        testAlbum = Album.builder().id(1L).title("Test Album").artist(testArtist).price(new BigDecimal("19.99"))
                .stockQuantity(10).build();

        // Create test order
        testOrder = Order.builder().id(1L).customer(testCustomer).status(OrderStatus.PENDING)
                .totalAmount(new BigDecimal("39.98")).orderDate(LocalDateTime.now()).orderItems(new ArrayList<>())
                .build();

        // Create test order response
        testOrderResponse = OrderResponse.builder().id(1L).customerId(1L).customerName("John Doe")
                .status(OrderStatus.PENDING).totalAmount(new BigDecimal("39.98")).build();
    }

    @Test
    void createOrder_withValidRequest_shouldCreateOrder()
    {
        // Arrange
        CreateOrderItemRequest itemRequest = CreateOrderItemRequest.builder().albumId(1L).quantity(2).build();

        CreateOrderRequest request = CreateOrderRequest.builder().customerId(1L).items(Arrays.asList(itemRequest))
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(albumRepository.findById(1L)).thenReturn(Optional.of(testAlbum));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(testOrderResponse);

        // Act
        OrderResponse result = orderService.createOrder(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals(new BigDecimal("39.98"), result.getTotalAmount());
        verify(albumRepository).save(testAlbum);
        assertEquals(8, testAlbum.getStockQuantity()); // 10 - 2
    }

    @Test
    void createOrder_withNonExistentCustomer_shouldThrowException()
    {
        // Arrange
        CreateOrderItemRequest itemRequest = CreateOrderItemRequest.builder().albumId(1L).quantity(2).build();

        CreateOrderRequest request = CreateOrderRequest.builder().customerId(999L).items(Arrays.asList(itemRequest))
                .build();

        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_withNonExistentAlbum_shouldThrowException()
    {
        // Arrange
        CreateOrderItemRequest itemRequest = CreateOrderItemRequest.builder().albumId(999L).quantity(2).build();

        CreateOrderRequest request = CreateOrderRequest.builder().customerId(1L).items(Arrays.asList(itemRequest))
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(albumRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_withInsufficientStock_shouldThrowException()
    {
        // Arrange
        CreateOrderItemRequest itemRequest = CreateOrderItemRequest.builder().albumId(1L).quantity(20).build();

        CreateOrderRequest request = CreateOrderRequest.builder().customerId(1L).items(Arrays.asList(itemRequest))
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(albumRepository.findById(1L)).thenReturn(Optional.of(testAlbum));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void getOrderById_withExistingId_shouldReturnOrder() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderMapper.toResponse(testOrder)).thenReturn(testOrderResponse);

        // Act
        OrderResponse result = orderService.getOrderById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(orderRepository).findById(1L);
    }

    @Test
    void getOrderById_withNonExistentId_shouldThrowException() {
        // Arrange
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> orderService.getOrderById(999L));
    }

    @Test
    void getOrdersByCustomer_shouldReturnOrders()
    {
        // Arrange
        List<Order> orders = Arrays.asList(testOrder);
        List<OrderResponse> responses = Arrays.asList(testOrderResponse);

        when(orderRepository.findByCustomerId(1L)).thenReturn(orders);
        when(orderMapper.toResponseList(orders)).thenReturn(responses);

        // Act
        List<OrderResponse> result = orderService.getOrdersByCustomer(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository).findByCustomerId(1L);
    }

    @Test
    void updateOrderStatus_withValidStatus_shouldUpdateOrder() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(testOrderResponse);

        // Act
        OrderResponse result = orderService.updateOrderStatus(1L, OrderStatus.PROCESSING);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.PROCESSING, testOrder.getStatus());
        verify(orderRepository).save(testOrder);
    }

    @Test
    void updateOrderStatus_withCancelledOrder_shouldThrowException()
    {
        // Arrange
        testOrder.setStatus(OrderStatus.CANCELLED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> orderService.updateOrderStatus(1L, OrderStatus.PROCESSING));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void cancelOrder_withPendingOrder_shouldCancelAndRestoreStock()
    {
        // Arrange
        OrderItem orderItem = OrderItem.builder().id(1L).album(testAlbum).quantity(2).unitPrice(new BigDecimal("19.99"))
                .subtotal(new BigDecimal("39.98")).build();

        testOrder.addOrderItem(orderItem);
        testAlbum.setStockQuantity(8); // Already reduced

        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        orderService.cancelOrder(1L);

        // Assert
        assertEquals(OrderStatus.CANCELLED, testOrder.getStatus());
        assertEquals(10, testAlbum.getStockQuantity()); // Restored from 8 to 10
        verify(albumRepository).save(testAlbum);
        verify(orderRepository).save(testOrder);
    }

    @Test
    void cancelOrder_withShippedOrder_shouldThrowException()
    {
        // Arrange
        testOrder.setStatus(OrderStatus.SHIPPED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> orderService.cancelOrder(1L));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void cancelOrder_withNonExistentOrder_shouldThrowException() {
        // Arrange
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> orderService.cancelOrder(999L));
    }
}
