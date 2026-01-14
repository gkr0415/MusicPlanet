package com.music.music_inventory_api.mapper;

import com.music.music_inventory_api.dto.request.CreateOrderItemRequest;
import com.music.music_inventory_api.dto.response.OrderItemResponse;
import com.music.music_inventory_api.entity.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemResponse toResponse(OrderItem orderItem);

    OrderItem toEntity(CreateOrderItemRequest request);
}
