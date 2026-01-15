package com.music.music_inventory_api.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.music.music_inventory_api.dto.request.CreateCustomerRequest;
import com.music.music_inventory_api.dto.response.CustomerResponse;
import com.music.music_inventory_api.entity.Customer;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CustomerMapperTest
{

    @Autowired
    private CustomerMapper customerMapper;

    @Test
    void toResponse_withValidCustomer_shouldReturnCustomerResponse()
    {
        // Arrange
        Customer customer = Customer.builder().id(1L).email("test@example.com").firstName("John").lastName("Doe")
                .phone("1234567890").city("New York").country("USA").build();

        // Act
        CustomerResponse response = customerMapper.toResponse(customer);

        // Assert
        assertNotNull(response);
        assertEquals(customer.getId(), response.getId());
        assertEquals(customer.getEmail(), response.getEmail());
        assertEquals(customer.getFirstName(), response.getFirstName());
        assertEquals(customer.getLastName(), response.getLastName());
        assertEquals(customer.getPhone(), response.getPhone());
    }

    @Test
    void toEntity_withValidCreateRequest_shouldReturnCustomerEntity()
    {
        // Arrange
        CreateCustomerRequest request = CreateCustomerRequest.builder().email("jane@example.com").firstName("Jane")
                .lastName("Smith").phone("0987654321").address("123 Main St").city("Los Angeles").country("USA")
                .postalCode("90001").build();

        // Act
        Customer customer = customerMapper.toEntity(request);

        // Assert
        assertNotNull(customer);
        assertEquals(request.getEmail(), customer.getEmail());
        assertEquals(request.getFirstName(), customer.getFirstName());
        assertEquals(request.getLastName(), customer.getLastName());
        assertEquals(request.getCity(), customer.getCity());
    }

    @Test
    void toResponseList_withValidCustomerList_shouldReturnResponseList()
    {
        // Arrange
        List<Customer> customers = Arrays.asList(
                Customer.builder().id(1L).email("user1@test.com").firstName("User").lastName("One").build(),
                Customer.builder().id(2L).email("user2@test.com").firstName("User").lastName("Two").build());

        // Act
        List<CustomerResponse> responses = customerMapper.toResponseList(customers);

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("User", responses.get(0).getFirstName());
        assertEquals("Two", responses.get(1).getLastName());
    }
}
