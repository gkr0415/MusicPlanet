package com.music.music_inventory_api.mapper;

import com.music.music_inventory_api.dto.request.CreateOrderItemRequest;
import com.music.music_inventory_api.dto.response.OrderItemResponse;
import com.music.music_inventory_api.entity.OrderItem;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper
{

    @Mapping(source = "album.id", target = "albumId")
    @Mapping(source = "album.title", target = "albumTitle")
    OrderItemResponse toResponse(OrderItem orderItem);

    @Mapping(source = "albumId", target = "album.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "unitPrice", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OrderItem toEntity(CreateOrderItemRequest request);

    List<OrderItemResponse> toResponseList(List<OrderItem> orderItems);
}
