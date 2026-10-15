package com.project.marketplace.dto;

import com.project.marketplace.pojo.ProductCategory;
import com.project.marketplace.pojo.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponseDto(

        Integer id,

        String name,

        String description,

        BigDecimal price,

        String image,

        int quantity,

        ProductCategory category,

        ProductStatus status,

        String materials,

        String certifications,

        int co2,

        List<DiscountResponseDto> discounts,

        BigDecimal weight,

        Integer sellerId
) {
}
