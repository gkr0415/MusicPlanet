package com.music.music_inventory_api.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.music.music_inventory_api.dto.response.OrderItemResponse;
import com.music.music_inventory_api.entity.Album;
import com.music.music_inventory_api.entity.OrderItem;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class OrderItemMapperTest
{

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Test
    void toResponse_withValidOrderItem_shouldReturnOrderItemResponse()
    {
        // Arrange
        Album album = Album.builder().id(1L).title("Test Album").build();
        OrderItem orderItem = OrderItem.builder().id(1L).album(album).quantity(2).unitPrice(new BigDecimal("19.99"))
                .subtotal(new BigDecimal("39.98")).build();

        // Act
        OrderItemResponse response = orderItemMapper.toResponse(orderItem);

        // Assert
        assertNotNull(response);
        assertEquals(orderItem.getId(), response.getId());
        assertEquals(album.getId(), response.getAlbumId());
        assertEquals(album.getTitle(), response.getAlbumTitle());
        assertEquals(orderItem.getQuantity(), response.getQuantity());
        assertEquals(orderItem.getUnitPrice(), response.getUnitPrice());
        assertEquals(orderItem.getSubtotal(), response.getSubtotal());
    }

    @Test
    void toResponseList_withValidOrderItemList_shouldReturnResponseList()
    {
        // Arrange
        Album album = Album.builder().id(1L).title("Album").build();
        List<OrderItem> orderItems = Arrays.asList(
                OrderItem.builder().id(1L).album(album).quantity(1).unitPrice(new BigDecimal("10.00")).build(),
                OrderItem.builder().id(2L).album(album).quantity(2).unitPrice(new BigDecimal("15.00")).build());

        // Act
        List<OrderItemResponse> responses = orderItemMapper.toResponseList(orderItems);

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(1, responses.get(0).getQuantity());
        assertEquals(2, responses.get(1).getQuantity());
    }
}
