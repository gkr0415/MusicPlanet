package com.music.music_inventory_api.repository;

import com.music.music_inventory_api.entity.Order;
import com.music.music_inventory_api.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Order entity. Provides CRUD operations and custom
 * queries for order data access.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>
{
    List<Order> findByCustomerId(Long customerId);

    List<Order> findByStatus(OrderStatus status);

    /**
     * Custom query to get customer order statistics. One of the required @Query
     * examples. Returns total order count, total amount spent, and average order
     * value.
     *
     * @param customerId
     *            the customer ID
     * @return array containing [orderCount, totalAmount, avgOrderValue]
     */
    @Query("SELECT COUNT(o), " + "COALESCE(SUM(o.totalAmount), 0), " + "COALESCE(AVG(o.totalAmount), 0) "
            + "FROM Order o " + "WHERE o.customer.id = :customerId")
    Object[] getCustomerOrderStatistics(@Param("customerId") Long customerId);

    /**
     * Custom query to find orders by date range.
     *
     * @param startDate
     *            start date
     * @param endDate
     *            end date
     * @return list of orders within the date range
     */
    @Query("SELECT o FROM Order o " + "WHERE o.createdAt BETWEEN :startDate AND :endDate "
            + "ORDER BY o.createdAt DESC")
    List<Order> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Custom query to find orders by customer and status.
     *
     * @param customerId
     *            the customer ID
     * @param status
     *            the order status
     * @return list of orders matching the criteria
     */
    @Query("SELECT o FROM Order o " + "WHERE o.customer.id = :customerId " + "AND o.status = :status "
            + "ORDER BY o.createdAt DESC")
    List<Order> findByCustomerAndStatus(@Param("customerId") Long customerId, @Param("status") OrderStatus status);

    /**
     * Custom query to find orders with total amount greater than a threshold.
     *
     * @param threshold
     *            minimum order amount
     * @return list of orders above the threshold
     */
    @Query("SELECT o FROM Order o " + "WHERE o.totalAmount > :threshold " + "ORDER BY o.totalAmount DESC")
    List<Order> findOrdersAboveAmount(@Param("threshold") BigDecimal threshold);

    /**
     * Custom query to calculate total revenue in a date range.
     *
     * @param startDate
     *            start date
     * @param endDate
     *            end date
     * @return total revenue
     */
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o " + "WHERE o.createdAt BETWEEN :startDate AND :endDate "
            + "AND o.status != 'CANCELLED'")
    BigDecimal calculateTotalRevenue(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
