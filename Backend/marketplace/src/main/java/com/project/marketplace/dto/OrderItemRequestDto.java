package com.project.marketplace.dto;

import java.math.BigDecimal;

public record OrderItemRequestDto(

        Integer productId,

        int quantity,

        BigDecimal unitPrice
) {
}
