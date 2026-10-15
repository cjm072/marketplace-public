package com.project.marketplace.dto;

import com.project.marketplace.pojo.ProductCategory;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record ProductRequestDto(

        @NotBlank(message = "Product name cannot be blank")
        String name,

        @NotBlank(message = "Product description cannot be blank")
        String description,

        BigDecimal price,

        String image,

        int quantity,

        ProductCategory category,

        BigDecimal weight,

        String materials,

        String certifications,

        int co2,

        Integer sellerId
) {
}
