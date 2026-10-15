package com.project.marketplace.dto;

import com.project.marketplace.pojo.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VoucherResponseDto(

        Integer id,

        String name,

        String description,

        LocalDate startDate,

        LocalDate expiryDate,

        BigDecimal amount,

        ProductCategory category,

        int quantity
) {
}
