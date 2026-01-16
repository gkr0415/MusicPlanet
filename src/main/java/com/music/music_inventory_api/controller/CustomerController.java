package com.music.music_inventory_api.controller;

import com.music.music_inventory_api.dto.request.CreateCustomerRequest;
import com.music.music_inventory_api.dto.request.UpdateCustomerRequest;
import com.music.music_inventory_api.dto.response.CustomerResponse;
import com.music.music_inventory_api.dto.response.OrderResponse;
import com.music.music_inventory_api.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** REST controller for Customer operations. */
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Customer management APIs")
public class CustomerController
{
    private final CustomerService customerService;

    @PostMapping
    @Operation(summary = "Create a new customer", description = "Creates a new customer with unique email")
    @ApiResponse(responseCode = "201", description = "Customer created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid customer data or email already exists")
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request)
    {
        CustomerResponse response = customerService.createCustomer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Retrieves customer information by ID")
    @ApiResponse(responseCode = "200", description = "Customer found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class)))
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public ResponseEntity<CustomerResponse> getCustomerById(
            @Parameter(description = "Customer ID", example = "1") @PathVariable Long id)
    {
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieves all customers with pagination support")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved customers", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    public ResponseEntity<Page<CustomerResponse>> getAllCustomers(
            @Parameter(description = "Page number (0-indexed)", example = "0") @PageableDefault(size = 20) Pageable pageable)
    {
        Page<CustomerResponse> response = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get customer by email", description = "Retrieves customer information by email address")
    @ApiResponse(responseCode = "200", description = "Customer found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class)))
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public ResponseEntity<CustomerResponse> getCustomerByEmail(
            @Parameter(description = "Customer email", example = "john@example.com") @PathVariable String email)
    {
        CustomerResponse response = customerService.getCustomerByEmail(email);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer", description = "Updates an existing customer")
    @ApiResponse(responseCode = "200", description = "Customer updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid customer data or email already exists")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @Parameter(description = "Customer ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequest request)
    {
        CustomerResponse response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer", description = "Deletes a customer by ID")
    @ApiResponse(responseCode = "204", description = "Customer deleted successfully")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "Customer ID", example = "1") @PathVariable Long id)
    {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/orders")
    @Operation(summary = "Get customer orders", description = "Retrieves all orders for a specific customer")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved customer orders", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class)))
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public ResponseEntity<List<OrderResponse>> getCustomerOrders(
            @Parameter(description = "Customer ID", example = "1") @PathVariable Long id)
    {
        List<OrderResponse> response = customerService.getCustomerOrders(id);
        return ResponseEntity.ok(response);
    }
}
