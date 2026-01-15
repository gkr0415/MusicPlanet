package com.music.music_inventory_api.service.impl;

import com.music.music_inventory_api.dto.request.CreateCustomerRequest;
import com.music.music_inventory_api.dto.request.UpdateCustomerRequest;
import com.music.music_inventory_api.dto.response.CustomerResponse;
import com.music.music_inventory_api.dto.response.OrderResponse;
import com.music.music_inventory_api.entity.Customer;
import com.music.music_inventory_api.entity.Order;
import com.music.music_inventory_api.exception.EntityNotFoundException;
import com.music.music_inventory_api.mapper.CustomerMapper;
import com.music.music_inventory_api.mapper.OrderMapper;
import com.music.music_inventory_api.repository.CustomerRepository;
import com.music.music_inventory_api.repository.OrderRepository;
import com.music.music_inventory_api.service.CustomerService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of CustomerService. */
@Service
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("null") // Suppress false-positive null warnings from IDE
public class CustomerServiceImpl implements CustomerService
{
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final CustomerMapper customerMapper;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request)
    {
        log.info("Creating new customer with email: {}", request.getEmail());

        // Check if email already exists
        Optional<Customer> existingCustomer = customerRepository.findByEmailIgnoreCase(request.getEmail());
        if (existingCustomer.isPresent())
        {
            throw new IllegalArgumentException("Customer with email " + request.getEmail() + " already exists");
        }

        Customer customer = customerMapper.toEntity(request);
        Customer savedCustomer = customerRepository.save(customer);

        log.info("Successfully created customer with ID: {}", savedCustomer.getId());
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id)
    {
        log.info("Fetching customer with ID: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + id));

        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByEmail(String email)
    {
        log.info("Fetching customer with email: {}", email);

        Customer customer = customerRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with email: " + email));

        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getAllCustomers(Pageable pageable)
    {
        log.info("Fetching all customers - Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Customer> customers = customerRepository.findAll(pageable);

        log.info("Found {} customers", customers.getTotalElements());
        return customers.map(customerMapper::toResponse);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request)
    {
        log.info("Updating customer with ID: {}", id);

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + id));

        // If email is being changed, check for uniqueness
        if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(existingCustomer.getEmail()))
        {
            Optional<Customer> customerWithEmail = customerRepository.findByEmailIgnoreCase(request.getEmail());
            if (customerWithEmail.isPresent())
            {
                throw new IllegalArgumentException("Customer with email " + request.getEmail() + " already exists");
            }
        }

        customerMapper.updateEntityFromRequest(request, existingCustomer);
        Customer updatedCustomer = customerRepository.save(existingCustomer);

        log.info("Successfully updated customer with ID: {}", updatedCustomer.getId());
        return customerMapper.toResponse(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id)
    {
        log.info("Deleting customer with ID: {}", id);

        if (!customerRepository.existsById(id))
        {
            throw new EntityNotFoundException("Customer not found with ID: " + id);
        }

        customerRepository.deleteById(id);
        log.info("Successfully deleted customer with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getCustomerOrders(Long customerId)
    {
        log.info("Fetching orders for customer ID: {}", customerId);

        // Verify customer exists
        if (!customerRepository.existsById(customerId))
        {
            throw new EntityNotFoundException("Customer not found with ID: " + customerId);
        }

        List<Order> orders = orderRepository.findByCustomerId(customerId);

        log.info("Found {} orders for customer ID: {}", orders.size(), customerId);
        return orders.stream().map(orderMapper::toResponse).collect(Collectors.toList());
    }
}
