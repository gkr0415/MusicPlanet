package com.music.music_inventory_api.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.music.music_inventory_api.dto.response.OrderResponse;
import com.music.music_inventory_api.entity.Customer;
import com.music.music_inventory_api.entity.Order;
import com.music.music_inventory_api.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class OrderMapperTest
{

    @Autowired
    private OrderMapper orderMapper;

    @Test
    void toResponse_withValidOrder_shouldReturnOrderResponse()
    {
        // Arrange
        Customer customer = Customer.builder().id(1L).firstName("John").lastName("Doe").password("pass").build();
        Order order = Order.builder().id(1L).customer(customer).orderDate(LocalDateTime.now())
                .totalAmount(new BigDecimal("99.99")).status(OrderStatus.PENDING).build();

        // Act
        OrderResponse response = orderMapper.toResponse(order);

        // Assert
        assertNotNull(response);
        assertEquals(order.getId(), response.getId());
        assertEquals(customer.getId(), response.getCustomerId());
        assertEquals(customer.getFirstName() + " " + customer.getLastName(), response.getCustomerName());
        assertEquals(order.getTotalAmount(), response.getTotalAmount());
        assertEquals(order.getStatus(), response.getStatus());
    }

    @Test
    void toResponseList_withValidOrderList_shouldReturnResponseList()
    {
        // Arrange
        Customer customer = Customer.builder().id(1L).firstName("Test").lastName("User").password("pass").build();
        List<Order> orders = Arrays.asList(
                Order.builder().id(1L).customer(customer).totalAmount(new BigDecimal("10.00"))
                        .status(OrderStatus.PENDING).build(),
                Order.builder().id(2L).customer(customer).totalAmount(new BigDecimal("20.00"))
                        .status(OrderStatus.SHIPPED).build());

        // Act
        List<OrderResponse> responses = orderMapper.toResponseList(orders);

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(OrderStatus.PENDING, responses.get(0).getStatus());
        assertEquals(OrderStatus.SHIPPED, responses.get(1).getStatus());
    }
}
