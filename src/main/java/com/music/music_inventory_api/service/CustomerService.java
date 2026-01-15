package com.music.music_inventory_api.service;

import com.music.music_inventory_api.dto.request.CreateCustomerRequest;
import com.music.music_inventory_api.dto.request.UpdateCustomerRequest;
import com.music.music_inventory_api.dto.response.CustomerResponse;
import com.music.music_inventory_api.dto.response.OrderResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/** Service interface for Customer operations. */
public interface CustomerService
{
    /** Create a new customer. */
    CustomerResponse createCustomer(CreateCustomerRequest request);

    /** Get customer by ID. */
    CustomerResponse getCustomerById(Long id);

    /** Get customer by email. */
    CustomerResponse getCustomerByEmail(String email);

    /** Get all customers with pagination. */
    Page<CustomerResponse> getAllCustomers(Pageable pageable);

    /** Update an existing customer. */
    CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request);

    /** Delete a customer. */
    void deleteCustomer(Long id);

    /** Get all orders for a customer. */
    List<OrderResponse> getCustomerOrders(Long customerId);
}
