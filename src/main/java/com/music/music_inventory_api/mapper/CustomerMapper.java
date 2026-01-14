package com.music.music_inventory_api.mapper;

import com.music.music_inventory_api.dto.request.CreateCustomerRequest;
import com.music.music_inventory_api.dto.response.CustomerDetailResponse;
import com.music.music_inventory_api.dto.response.CustomerResponse;
import com.music.music_inventory_api.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponse toResponse(Customer customer);

    CustomerDetailResponse toDetailResponse(Customer customer);

    Customer toEntity(CreateCustomerRequest request);
}
