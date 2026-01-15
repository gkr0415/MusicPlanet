package com.music.music_inventory_api.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO for order item response. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse
{

    private Long id;
    private Long albumId;
    private String albumTitle;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}
