package com.project.marketplace.dto;

import java.math.BigDecimal;

public record OrderItemResponseDto(

        Integer id,

        Integer orderId,

        Integer productId,

        String productName,

        String productImage,

        int quantity,

        BigDecimal unitPrice,

        BigDecimal subtotal,

        Integer sellerId
) {
}
