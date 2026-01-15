package com.music.music_inventory_api.mapper;

import com.music.music_inventory_api.dto.request.CreateCustomerRequest;
import com.music.music_inventory_api.dto.response.CustomerDetailResponse;
import com.music.music_inventory_api.dto.response.CustomerResponse;
import com.music.music_inventory_api.entity.Customer;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses =
{OrderMapper.class})
public interface CustomerMapper
{

    CustomerResponse toResponse(Customer customer);

    @Mapping(target = "orders", ignore = true)
    CustomerDetailResponse toDetailResponse(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Customer toEntity(CreateCustomerRequest request);

    List<CustomerResponse> toResponseList(List<Customer> customers);
}
