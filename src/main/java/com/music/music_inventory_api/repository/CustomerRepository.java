package com.music.music_inventory_api.repository;

import com.music.music_inventory_api.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Customer entity.
 * Provides CRUD operations and custom queries for customer data access.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> 
{
    Optional<Customer> findByEmailIgnoreCase(String email);

    /**
     * Custom query to search customers by name.
     *
     * @param keyword the search keyword
     * @return list of customers matching the search criteria
     */
    @Query("SELECT c FROM Customer c " +
           "WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY c.lastName ASC, c.firstName ASC")
    List<Customer> searchByName(@Param("keyword") String keyword);

    /**
     * Custom query to find customers who placed orders after a specific date.
     *
     * @param date the date to filter by
     * @return list of customers with orders after the specified date
     */
    @Query("SELECT DISTINCT o.customer FROM Order o " +
           "WHERE o.createdAt > :date " +
           "ORDER BY o.customer.lastName ASC")
    List<Customer> findCustomersWithOrdersAfter(@Param("date") LocalDateTime date);

    /**
     * Custom query to find customers with no orders.
     *
     * @return list of customers who have never placed an order
     */
    @Query("SELECT c FROM Customer c " +
           "WHERE NOT EXISTS (SELECT 1 FROM Order o WHERE o.customer.id = c.id)")
    List<Customer> findCustomersWithNoOrders();

    /**
     * Custom query to find top customers by order count.
     *
     * @return list of customers ordered by number of orders descending
     */
    @Query("SELECT o.customer FROM Order o " +
           "GROUP BY o.customer " +
           "ORDER BY COUNT(o) DESC")
    List<Customer> findTopCustomersByOrderCount();
}

