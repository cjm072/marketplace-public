package com.project.marketplace.service;

import com.project.marketplace.dto.OrderRequestDto;
import com.project.marketplace.dto.OrderResponseDto;
import com.project.marketplace.pojo.OrderStatus;

import java.util.List;

public interface OrderService {
    List<OrderResponseDto> getAllOrdersByCustomer(Integer customerId);
    List<OrderResponseDto> getAllOrdersBySeller(Integer sellerId);
    OrderResponseDto addOrder(OrderRequestDto dto);
    OrderResponseDto updateOrderStatus(OrderStatus status, Integer orderId);
}
