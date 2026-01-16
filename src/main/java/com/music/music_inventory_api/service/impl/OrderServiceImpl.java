package com.music.music_inventory_api.service.impl;

import com.music.music_inventory_api.dto.request.CreateOrderItemRequest;
import com.music.music_inventory_api.dto.request.CreateOrderRequest;
import com.music.music_inventory_api.dto.response.OrderResponse;
import com.music.music_inventory_api.entity.Album;
import com.music.music_inventory_api.entity.Customer;
import com.music.music_inventory_api.entity.Order;
import com.music.music_inventory_api.entity.OrderItem;
import com.music.music_inventory_api.enums.OrderStatus;
import com.music.music_inventory_api.exception.EntityNotFoundException;
import com.music.music_inventory_api.mapper.OrderMapper;
import com.music.music_inventory_api.repository.AlbumRepository;
import com.music.music_inventory_api.repository.CustomerRepository;
import com.music.music_inventory_api.repository.OrderRepository;
import com.music.music_inventory_api.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Implementation of OrderService interface. */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService
{

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final AlbumRepository albumRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse createOrder(CreateOrderRequest request)
    {
        log.info("Creating new order for customer ID: {}", request.getCustomerId());

        // Validate customer exists
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer", request.getCustomerId()));

        // Create order
        Order order = Order.builder().customer(customer).status(OrderStatus.PENDING)
                .orderDate(LocalDateTime.now()).totalAmount(BigDecimal.ZERO).build();

        // Process order items
        BigDecimal total = BigDecimal.ZERO;
        for (CreateOrderItemRequest itemRequest : request.getItems())
        {
            // Validate album exists
            Album album = albumRepository.findById(itemRequest.getAlbumId())
                    .orElseThrow(() -> new EntityNotFoundException("Album", itemRequest.getAlbumId()));

            // Validate stock
            if (album.getStockQuantity() < itemRequest.getQuantity())
            {
                throw new IllegalArgumentException("Insufficient stock for album: " + album.getTitle()
                        + ". Available: " + album.getStockQuantity() + ", Requested: " + itemRequest.getQuantity());
            }

            // Calculate subtotal
            BigDecimal subtotal = album.getPrice().multiply(new BigDecimal(itemRequest.getQuantity()));

            // Create order item
            OrderItem orderItem = OrderItem.builder().album(album).quantity(itemRequest.getQuantity())
                    .unitPrice(album.getPrice()).subtotal(subtotal).build();

            order.addOrderItem(orderItem);

            // Reduce stock
            album.setStockQuantity(album.getStockQuantity() - itemRequest.getQuantity());
            albumRepository.save(album);

            // Add to total
            total = total.add(subtotal);

            log.debug("Added item to order: Album={}, Quantity={}, Subtotal={}", album.getTitle(),
                    itemRequest.getQuantity(), subtotal);
        }

        order.setTotalAmount(total);
        Order savedOrder = orderRepository.save(order);

        log.info("Successfully created order with ID: {}, Total: {}", savedOrder.getId(), total);
        return orderMapper.toResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(Long id)
    {
        log.info("Fetching order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order", id));
        return orderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getOrdersByCustomer(Long customerId)
    {
        log.info("Fetching orders for customer ID: {}", customerId);
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        log.info("Found {} orders for customer ID: {}", orders.size(), customerId);
        return orderMapper.toResponseList(orders);
    }

    @Override
    public OrderResponse updateOrderStatus(Long id, OrderStatus status)
    {
        log.info("Updating order ID: {} to status: {}", id, status);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order", id));

        // Validate status transition
        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED)
        {
            throw new IllegalStateException(
                    "Cannot update status of " + order.getStatus() + " order. Current status: " + order.getStatus());
        }

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        log.info("Successfully updated order ID: {} to status: {}", id, status);
        return orderMapper.toResponse(updatedOrder);
    }

    @Override
    public void cancelOrder(Long id)
    {
        log.info("Cancelling order with ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order", id));

        // Validate order can be cancelled
        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED)
        {
            throw new IllegalStateException(
                    "Cannot cancel order with status: " + order.getStatus() + ". Order already shipped or delivered.");
        }

        if (order.getStatus() == OrderStatus.CANCELLED)
        {
            log.warn("Order ID: {} is already cancelled", id);
            return;
        }

        // Restore stock for all items
        for (OrderItem item : order.getOrderItems())
        {
            Album album = item.getAlbum();
            album.setStockQuantity(album.getStockQuantity() + item.getQuantity());
            albumRepository.save(album);
            log.debug("Restored {} units of album: {}", item.getQuantity(), album.getTitle());
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        log.info("Successfully cancelled order with ID: {}", id);
    }
}
