package com.project.marketplace.service;

import com.project.marketplace.dto.CustomerRequestDto;
import com.project.marketplace.dto.CustomerResponseDto;
import com.project.marketplace.dto.SellerRequestDto;
import com.project.marketplace.mapper.CustomerMapper;
import com.project.marketplace.pojo.Customer;
import com.project.marketplace.pojo.Seller;
import com.project.marketplace.repository.CustomerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerResponseDto getCustomerById(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer with ID " + customerId + " not found"));
        return customerMapper.toCustomerResponseDto(customer);
    }

    @Override
    public CustomerResponseDto addCustomer(CustomerRequestDto dto) {
        Customer customer = customerMapper.toCustomer(dto);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerResponseDto(savedCustomer);
    }

    @Override
    public CustomerResponseDto updateCustomer(CustomerRequestDto dto, Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer with ID " + customerId + " not found"));
        customerMapper.updateCustomerFromDto(dto, customer);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerResponseDto(savedCustomer);
    }

    @Override
    public void deleteCustomer(Integer customerId){
        customerRepository.deleteById(customerId);
    }

    @Override
    @Transactional
    public void register(CustomerRequestDto dto) {
        Customer customer = customerMapper.toCustomer(dto);
        customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
    }
}
