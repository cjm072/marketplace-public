package com.project.marketplace.service;

import com.project.marketplace.dto.PaymentRequestDto;
import com.project.marketplace.dto.PaymentResponseDto;
import com.project.marketplace.mapper.PaymentMapper;
import com.project.marketplace.pojo.Customer;
import com.project.marketplace.pojo.Payment;
import com.project.marketplace.repository.CustomerRepository;
import com.project.marketplace.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final CustomerRepository customerRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper, CustomerRepository customerRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public PaymentResponseDto getPaymentById(Integer paymentId){
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoSuchElementException("Payment with ID " + paymentId + " not found"));
        return paymentMapper.toPaymentResponseDto(payment);
    }

    @Override
    public List<PaymentResponseDto> getAllPaymentsByCustomer(Integer customerId){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer with ID " + customerId + " not found"));
        List<Payment> payments = paymentRepository.findAllByCustomer(customer);
        return paymentMapper.toPaymentResponseDtoList(payments);
    }

    @Override
    public PaymentResponseDto addPayment(PaymentRequestDto dto){
        Payment payment = paymentMapper.toPayment(dto);
        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toPaymentResponseDto(savedPayment);
    }

    @Override
    public Map<Integer, Long> getPaymentCountPerMonth(int year) {
        List<Object[]> results = paymentRepository.countPaymentsPerMonth(year);

        Map<Integer, Long> monthCounts = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            monthCounts.put(i, 0L);
        }

        for (Object[] row : results) {
            Integer month = (Integer) row[0];
            Long count = (Long) row[1];
            monthCounts.put(month, count);
        }

        return monthCounts;
    }

    @Override
    public Map<Integer, BigDecimal> getPaymentAmountPerMonth(int year) {
        List<Object[]> results = paymentRepository.sumPaymentAmountPerMonth(year);

        Map<Integer, BigDecimal> monthAmounts = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            monthAmounts.put(i, BigDecimal.ZERO);
        }

        for (Object[] row : results) {
            Integer month = (Integer) row[0];
            BigDecimal amount = (BigDecimal) row[1];
            monthAmounts.put(month, amount);
        }

        return monthAmounts;
    }

    @Override
    public Map<Integer, BigDecimal> getAveragePaymentPerMonth(int year) {
        List<Object[]> results = paymentRepository.averageAmountPerMonth(year);

        Map<Integer, BigDecimal> averageAmounts = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            averageAmounts.put(i, BigDecimal.ZERO);
        }

        for (Object[] row : results) {
            Integer month = (Integer) row[0];
            Double avg = (Double) row[1];
            BigDecimal avgAmount = BigDecimal.valueOf(avg);
            averageAmounts.put(month, avgAmount);
        }

        return averageAmounts;
    }

    @Override
    public Map<Integer, Long> getPaymentCountPerMonthBySeller(int year, Integer sellerId) {
        List<Object[]> results = paymentRepository.countPaymentsPerMonthBySeller(year, sellerId);

        Map<Integer, Long> monthCounts = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            monthCounts.put(i, 0L);
        }

        for (Object[] row : results) {
            Integer month = (Integer) row[0];
            Long count = (Long) row[1];
            monthCounts.put(month, count);
        }

        return monthCounts;
    }

    @Override
    public Map<Integer, BigDecimal> getPaymentAmountPerMonthBySeller(int year, Integer sellerId) {
        List<Object[]> results = paymentRepository.sumPaymentAmountPerMonthBySeller(year, sellerId);

        Map<Integer, BigDecimal> monthAmounts = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            monthAmounts.put(i, BigDecimal.ZERO);
        }

        for (Object[] row : results) {
            Integer month = (Integer) row[0];
            BigDecimal amount = (BigDecimal) row[1];
            monthAmounts.put(month, amount);
        }

        return monthAmounts;
    }

    @Override
    public Map<Integer, BigDecimal> getAveragePaymentPerMonthBySeller(int year, Integer sellerId) {
        List<Object[]> results = paymentRepository.averageAmountPerMonthBySeller(year, sellerId);

        Map<Integer, BigDecimal> averageAmounts = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            averageAmounts.put(i, BigDecimal.ZERO);
        }

        for (Object[] row : results) {
            Integer month = (Integer) row[0];
            Double avg = (Double) row[1];
            BigDecimal avgAmount = BigDecimal.valueOf(avg);
            averageAmounts.put(month, avgAmount);
        }

        return averageAmounts;
    }


}
