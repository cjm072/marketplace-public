package com.project.marketplace.dto;

import com.project.marketplace.pojo.Address;
import com.project.marketplace.pojo.DeliveryMethod;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DeliveryResponseDto(

        Integer id,

        DeliveryMethod method,

        BigDecimal price,

        BigDecimal emissionRate,

        String courier,

        LocalDate dispatchedAt,

        LocalDate deliveredAt,

        Address shippingAddress
) {
}
