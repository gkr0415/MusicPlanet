package com.music.music_inventory_api.mapper;

import com.music.music_inventory_api.dto.request.CreateOrderRequest;
import com.music.music_inventory_api.dto.response.OrderResponse;
import com.music.music_inventory_api.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toResponse(Order order);

    Order toEntity(CreateOrderRequest request);
}
