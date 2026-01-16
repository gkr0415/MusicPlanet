package com.music.music_inventory_api.mapper;

import com.music.music_inventory_api.dto.request.CreateOrderRequest;
import com.music.music_inventory_api.dto.response.OrderResponse;
import com.music.music_inventory_api.entity.Order;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses =
{OrderItemMapper.class})
public interface OrderMapper
{

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(target = "customerName", expression = "java(order.getCustomer().getFirstName() + \" \" + order.getCustomer().getLastName())")
    @Mapping(source = "orderItems", target = "items")
    OrderResponse toResponse(Order order);

    @Mapping(source = "customerId", target = "customer.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "shippingAddress", ignore = true)
    @Mapping(target = "shippingCity", ignore = true)
    @Mapping(target = "shippingCountry", ignore = true)
    @Mapping(target = "shippingPostalCode", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toEntity(CreateOrderRequest request);

    List<OrderResponse> toResponseList(List<Order> orders);
}
