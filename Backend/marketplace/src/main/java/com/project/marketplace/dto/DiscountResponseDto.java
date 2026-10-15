package com.project.marketplace.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DiscountResponseDto(

        Integer id,

        String name,

        String description,

        BigDecimal amount,

        LocalDate startDate,

        LocalDate expiryDate,

        Integer sellerId
) {
}
