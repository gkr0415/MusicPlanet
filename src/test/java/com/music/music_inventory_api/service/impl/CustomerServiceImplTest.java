package com.music.music_inventory_api.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/** Unit tests for CustomerServiceImpl. */
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest
{
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer testCustomer;
    private CustomerResponse testCustomerResponse;
    private CreateCustomerRequest createRequest;
    private UpdateCustomerRequest updateRequest;

    @BeforeEach
    void setUp()
    {
        testCustomer = Customer.builder().id(1L).firstName("John").lastName("Doe").email("john@example.com")
                .phone("1234567890").address("123 Main St").build();

        testCustomerResponse = CustomerResponse.builder().id(1L).firstName("John").lastName("Doe")
                .email("john@example.com").phone("1234567890").address("123 Main St").build();

        createRequest = CreateCustomerRequest.builder().firstName("John").lastName("Doe").email("john@example.com")
                .phone("1234567890").address("123 Main St").build();

        updateRequest = UpdateCustomerRequest.builder().firstName("Jane").lastName("Smith").email("jane@example.com")
                .phone("0987654321").address("456 Oak Ave").build();
    }

    @Test
    void createCustomer_withUniqueEmail_shouldReturnCustomerResponse()
    {
        // Arrange
        when(customerRepository.findByEmailIgnoreCase(createRequest.getEmail())).thenReturn(Optional.empty());
        when(customerMapper.toEntity(createRequest)).thenReturn(testCustomer);
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
        when(customerMapper.toResponse(testCustomer)).thenReturn(testCustomerResponse);

        // Act
        CustomerResponse result = customerService.createCustomer(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testCustomerResponse.getEmail(), result.getEmail());
        verify(customerRepository).findByEmailIgnoreCase(createRequest.getEmail());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void createCustomer_withDuplicateEmail_shouldThrowIllegalArgumentException()
    {
        // Arrange
        when(customerRepository.findByEmailIgnoreCase(createRequest.getEmail())).thenReturn(Optional.of(testCustomer));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(createRequest));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void getCustomerById_withExistingId_shouldReturnCustomerResponse()
    {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(customerMapper.toResponse(testCustomer)).thenReturn(testCustomerResponse);

        // Act
        CustomerResponse result = customerService.getCustomerById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testCustomerResponse.getId(), result.getId());
        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerById_withNonExistentId_shouldThrowEntityNotFoundException()
    {
        // Arrange
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> customerService.getCustomerById(999L));
    }

    @Test
    void getCustomerByEmail_withExistingEmail_shouldReturnCustomerResponse()
    {
        // Arrange
        when(customerRepository.findByEmailIgnoreCase("john@example.com")).thenReturn(Optional.of(testCustomer));
        when(customerMapper.toResponse(testCustomer)).thenReturn(testCustomerResponse);

        // Act
        CustomerResponse result = customerService.getCustomerByEmail("john@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(testCustomerResponse.getEmail(), result.getEmail());
        verify(customerRepository).findByEmailIgnoreCase("john@example.com");
    }

    @Test
    void getCustomerByEmail_withNonExistentEmail_shouldThrowEntityNotFoundException()
    {
        // Arrange
        when(customerRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> customerService.getCustomerByEmail("nonexistent@example.com"));
    }

    @Test
    void getAllCustomers_withPageable_shouldReturnPageOfCustomers()
    {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> customerPage = new PageImpl<>(Collections.singletonList(testCustomer));
        when(customerRepository.findAll(pageable)).thenReturn(customerPage);
        when(customerMapper.toResponse(any(Customer.class))).thenReturn(testCustomerResponse);

        // Act
        Page<CustomerResponse> result = customerService.getAllCustomers(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(customerRepository).findAll(pageable);
    }

    @Test
    void updateCustomer_withValidData_shouldReturnUpdatedCustomer()
    {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.findByEmailIgnoreCase(updateRequest.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
        when(customerMapper.toResponse(testCustomer)).thenReturn(testCustomerResponse);

        // Act
        CustomerResponse result = customerService.updateCustomer(1L, updateRequest);

        // Assert
        assertNotNull(result);
        verify(customerMapper).updateEntityFromRequest(updateRequest, testCustomer);
        verify(customerRepository).save(testCustomer);
    }

    @Test
    void updateCustomer_withDuplicateEmail_shouldThrowIllegalArgumentException()
    {
        // Arrange
        Customer anotherCustomer = Customer.builder().id(2L).email("jane@example.com").build();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.findByEmailIgnoreCase(updateRequest.getEmail()))
                .thenReturn(Optional.of(anotherCustomer));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> customerService.updateCustomer(1L, updateRequest));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void updateCustomer_withNonExistentId_shouldThrowEntityNotFoundException()
    {
        // Arrange
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> customerService.updateCustomer(999L, updateRequest));
    }

    @Test
    void deleteCustomer_withExistingId_shouldDeleteCustomer()
    {
        // Arrange
        when(customerRepository.existsById(1L)).thenReturn(true);

        // Act
        customerService.deleteCustomer(1L);

        // Assert
        verify(customerRepository).deleteById(1L);
    }

    @Test
    void deleteCustomer_withNonExistentId_shouldThrowEntityNotFoundException()
    {
        // Arrange
        when(customerRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> customerService.deleteCustomer(999L));
        verify(customerRepository, never()).deleteById(anyLong());
    }

    @Test
    void getCustomerOrders_withExistingCustomer_shouldReturnOrdersList()
    {
        // Arrange
        Order order1 = Order.builder().id(1L).build();
        Order order2 = Order.builder().id(2L).build();
        OrderResponse orderResponse1 = OrderResponse.builder().id(1L).build();
        OrderResponse orderResponse2 = OrderResponse.builder().id(2L).build();

        when(customerRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.findByCustomerId(1L)).thenReturn(Arrays.asList(order1, order2));
        when(orderMapper.toResponse(order1)).thenReturn(orderResponse1);
        when(orderMapper.toResponse(order2)).thenReturn(orderResponse2);

        // Act
        List<OrderResponse> result = customerService.getCustomerOrders(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository).findByCustomerId(1L);
    }

    @Test
    void getCustomerOrders_withNonExistentCustomer_shouldThrowEntityNotFoundException()
    {
        // Arrange
        when(customerRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> customerService.getCustomerOrders(999L));
        verify(orderRepository, never()).findByCustomerId(anyLong());
    }

    @Test
    void getCustomerOrders_withNoOrders_shouldReturnEmptyList()
    {
        // Arrange
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.findByCustomerId(1L)).thenReturn(Collections.emptyList());

        // Act
        List<OrderResponse> result = customerService.getCustomerOrders(1L);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
