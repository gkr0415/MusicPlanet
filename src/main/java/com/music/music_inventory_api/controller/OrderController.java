package com.music.music_inventory_api.controller;

import com.music.music_inventory_api.dto.request.CreateOrderRequest;
import com.music.music_inventory_api.dto.response.OrderResponse;
import com.music.music_inventory_api.enums.OrderStatus;
import com.music.music_inventory_api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** REST controller for Order management. */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Order", description = "Order management APIs")
public class OrderController
{

    private final OrderService orderService;

    /**
     * Create a new order.
     *
     * @param request
     *            the order creation request
     * @return the created order
     */
    @PostMapping
    @Operation(summary = "Create a new order", description = "Creates a new order with order processing logic")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or insufficient stock"),
            @ApiResponse(responseCode = "404", description = "Customer or album not found")})
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request)
    {
        log.info("Creating new order for customer ID: {}", request.getCustomerId());
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get order by ID.
     *
     * @param id
     *            the order ID
     * @return the order
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieves an order by its ID")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")})
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id)
    {
        log.info("Fetching order with ID: {}", id);
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get customer orders.
     *
     * @param customerId
     *            the customer ID
     * @return list of customer orders
     */
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get customer orders", description = "Retrieves all orders for a specific customer")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Orders retrieved successfully")})
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable Long customerId)
    {
        log.info("Fetching orders for customer ID: {}", customerId);
        List<OrderResponse> responses = orderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Update order status.
     *
     * @param id
     *            the order ID
     * @param status
     *            the new order status
     * @return the updated order
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "Update order status", description = "Updates the status of an order")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status transition"),
            @ApiResponse(responseCode = "404", description = "Order not found")})
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status)
    {
        log.info("Updating order ID: {} to status: {}", id, status);
        OrderResponse response = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(response);
    }

    /**
     * Cancel order.
     *
     * @param id
     *            the order ID
     * @return no content
     */
    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel order", description = "Cancels an order and restores stock")
    @ApiResponses(value =
    {@ApiResponse(responseCode = "204", description = "Order cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Order cannot be cancelled"),
            @ApiResponse(responseCode = "404", description = "Order not found")})
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id)
    {
        log.info("Cancelling order with ID: {}", id);
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}
