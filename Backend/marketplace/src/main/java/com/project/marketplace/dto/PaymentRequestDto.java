package com.project.marketplace.dto;

import com.project.marketplace.pojo.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentRequestDto(

        PaymentMethod method,

        BigDecimal amount,

        LocalDate paymentDate
) {
}
