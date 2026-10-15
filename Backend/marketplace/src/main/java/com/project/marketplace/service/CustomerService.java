package com.project.marketplace.service;

import com.project.marketplace.dto.CustomerRequestDto;
import com.project.marketplace.dto.CustomerResponseDto;

public interface CustomerService {
    CustomerResponseDto getCustomerById(Integer customerId);
    CustomerResponseDto addCustomer(CustomerRequestDto dto);
    CustomerResponseDto updateCustomer(CustomerRequestDto dto, Integer customerId);
    void deleteCustomer(Integer customerId);
    void register(CustomerRequestDto dto);
}
