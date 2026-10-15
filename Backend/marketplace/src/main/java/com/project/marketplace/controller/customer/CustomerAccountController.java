package com.project.marketplace.controller.customer;

import com.project.marketplace.dto.CustomerRequestDto;
import com.project.marketplace.dto.CustomerResponseDto;
import com.project.marketplace.dto.UserRequestDto;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.CustomerService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/customer/account")
public class CustomerAccountController {

    private final CustomerService customerService;

    public CustomerAccountController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{customerId}")
    public ApiResponse<CustomerResponseDto> getCustomerById(@PathVariable("customerId") Integer customerId) {
        CustomerResponseDto customer = customerService.getCustomerById(customerId);
        return ApiResponse.success(customer);
    }

    @PostMapping
    public ApiResponse<CustomerResponseDto> addCustomer(@RequestBody CustomerRequestDto dto) {
        CustomerResponseDto newCustomer = customerService.addCustomer(dto);
        return ApiResponse.success(newCustomer);
    }

    @PutMapping("/{customerId}")
    public ApiResponse<CustomerResponseDto> updateCustomer(@RequestBody CustomerRequestDto dto, @PathVariable("customerId") Integer customerId) {
        CustomerResponseDto updatedCustomer = customerService.updateCustomer(dto, customerId);
        return ApiResponse.success(updatedCustomer);
    }

    @DeleteMapping("/{customerId}")
    public ApiResponse<Void> deleteCustomer(@PathVariable("customerId") Integer customerId) {
        customerService.deleteCustomer(customerId);
        return ApiResponse.success();
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody CustomerRequestDto dto) {
        customerService.register(dto);
        return ApiResponse.success();
    }
}
