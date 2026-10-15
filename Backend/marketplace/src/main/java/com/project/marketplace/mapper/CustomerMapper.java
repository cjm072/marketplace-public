package com.project.marketplace.mapper;

import com.project.marketplace.dto.CustomerRequestDto;
import com.project.marketplace.dto.CustomerResponseDto;
import com.project.marketplace.pojo.Customer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerMapper {
    public Customer toCustomer(CustomerRequestDto dto){
        if ( dto == null ) {
            return null;
        }
        Customer customer = new Customer();
        updateCustomerFromDto(dto, customer);
        return customer;
    }

    public void updateCustomerFromDto(CustomerRequestDto dto, Customer customer){
        customer.setUsername(dto.getUsername());
        customer.setPassword(dto.getPassword());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setRole(dto.getRole());
    }

    public CustomerResponseDto toCustomerResponseDto(Customer customer){
        if ( customer == null ) {
            return null;
        }

        return new CustomerResponseDto(
                customer.getId(),
                customer.getUsername(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getImage(),
                customer.getAddress()
        );
    }

    public List<CustomerResponseDto> toCustomerResponseDtoList(List<Customer> customers){
        if ( customers == null ) {
            return null;
        }
        List<CustomerResponseDto> list = new ArrayList<>( customers.size() );
        for ( Customer customer : customers ) {
            list.add( toCustomerResponseDto(customer) );
        }

        return list;
    }
}
