package com.project.marketplace.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DiscountRequestDto(

        String name,

        String description,

        BigDecimal amount,

        LocalDate startDate,

        LocalDate expiryDate,

        Integer sellerId
) {
}
