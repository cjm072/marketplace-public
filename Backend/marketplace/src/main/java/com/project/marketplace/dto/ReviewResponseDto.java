package com.project.marketplace.dto;

import java.time.LocalDate;

public record ReviewResponseDto(

        Integer id,

        String comment,

        int rating,

        LocalDate reviewDate,

        Integer product_id,

        Integer customer_id
) {
}
