package com.project.marketplace.service;

import com.project.marketplace.dto.PaymentRequestDto;
import com.project.marketplace.dto.PaymentResponseDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PaymentService {
    PaymentResponseDto getPaymentById(Integer paymentId);
    List<PaymentResponseDto> getAllPaymentsByCustomer(Integer customerId);
    PaymentResponseDto addPayment(PaymentRequestDto dto);
    Map<Integer, Long> getPaymentCountPerMonth(int year);
    Map<Integer, BigDecimal> getPaymentAmountPerMonth(int year);
    Map<Integer, BigDecimal> getAveragePaymentPerMonth(int year);
    Map<Integer, Long> getPaymentCountPerMonthBySeller(int year, Integer sellerId);
    Map<Integer, BigDecimal> getPaymentAmountPerMonthBySeller(int year, Integer sellerId);
    Map<Integer, BigDecimal> getAveragePaymentPerMonthBySeller(int year, Integer sellerId);

}
