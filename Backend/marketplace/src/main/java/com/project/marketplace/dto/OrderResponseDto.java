package com.project.marketplace.dto;

import com.project.marketplace.pojo.OrderStatus;

import java.util.List;

public record OrderResponseDto(

        Integer id,

        OrderStatus orderStatus,

        boolean co2,

        DeliveryResponseDto delivery,

        PaymentResponseDto payment,

        VoucherResponseDto voucher,

        Integer customerId,

        List<OrderItemResponseDto> orderItems
) {
}
