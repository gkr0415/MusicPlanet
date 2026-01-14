package com.music.music_inventory_api.mapper;

import com.music.music_inventory_api.dto.request.CreateOrderRequest;
import com.music.music_inventory_api.dto.response.OrderResponse;
import com.music.music_inventory_api.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.firstName", target = "customerName")
    OrderResponse toResponse(Order order);

    @Mapping(source = "customerId", target = "customer.id")
    Order toEntity(CreateOrderRequest request);
}
