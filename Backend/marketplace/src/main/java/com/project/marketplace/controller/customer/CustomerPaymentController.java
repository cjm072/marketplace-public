package com.project.marketplace.controller.customer;

import com.project.marketplace.dto.PaymentRequestDto;
import com.project.marketplace.dto.PaymentResponseDto;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer/payment")
public class CustomerPaymentController {
    private final PaymentService paymentService;

    public CustomerPaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{paymentId}")
    public ApiResponse<PaymentResponseDto> getPaymentById(@PathVariable("paymentId") Integer paymentId){
        PaymentResponseDto payment = paymentService.getPaymentById(paymentId);
        return ApiResponse.success(payment);
    }

    @GetMapping("/batch/{customerId}")
    public ApiResponse<List<PaymentResponseDto>> getAllPaymentsByCustomer(@PathVariable("customerId") Integer customerId){
        List<PaymentResponseDto> payments = paymentService.getAllPaymentsByCustomer(customerId);
        return ApiResponse.success(payments);
    }

    @PostMapping
    public ApiResponse<PaymentResponseDto> addPayment(@RequestBody PaymentRequestDto dto){
        PaymentResponseDto newPayment = paymentService.addPayment(dto);
        return ApiResponse.success(newPayment);
    }
}
