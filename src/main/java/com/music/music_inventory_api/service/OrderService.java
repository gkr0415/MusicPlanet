package com.music.music_inventory_api.service;

import com.music.music_inventory_api.dto.request.CreateOrderRequest;
import com.music.music_inventory_api.dto.response.OrderResponse;
import com.music.music_inventory_api.enums.OrderStatus;
import java.util.List;

/**
 * Service interface for Order entity operations. Provides business logic for
 * order management including order processing, status updates, and retrieval.
 */
public interface OrderService
{

    /**
     * Creates a new order with order processing logic (validate stock, calculate
     * totals).
     *
     * @param request
     *            the order creation request containing customer ID and order items
     * @return the created order response
     * @throws com.music.music_inventory_api.exception.EntityNotFoundException
     *             if customer or album not found
     * @throws IllegalArgumentException
     *             if insufficient stock
     */
    OrderResponse createOrder(CreateOrderRequest request);

    /**
     * Retrieves an order by its ID.
     *
     * @param id
     *            the order ID
     * @return the order response
     * @throws com.music.music_inventory_api.exception.EntityNotFoundException
     *             if order not found
     */
    OrderResponse getOrderById(Long id);

    /**
     * Retrieves all orders for a specific customer.
     *
     * @param customerId
     *            the customer ID
     * @return list of order responses
     */
    List<OrderResponse> getOrdersByCustomer(Long customerId);

    /**
     * Updates the status of an order.
     *
     * @param id
     *            the order ID
     * @param status
     *            the new order status
     * @return the updated order response
     * @throws com.music.music_inventory_api.exception.EntityNotFoundException
     *             if order not found
     * @throws IllegalStateException
     *             if status transition is invalid
     */
    OrderResponse updateOrderStatus(Long id, OrderStatus status);

    /**
     * Cancels an order and restores stock.
     *
     * @param id
     *            the order ID
     * @throws com.music.music_inventory_api.exception.EntityNotFoundException
     *             if order not found
     * @throws IllegalStateException
     *             if order cannot be cancelled
     */
    void cancelOrder(Long id);
}
