package com.project.marketplace.dto;

import java.util.List;

public record OrderRequestDto(

        boolean co2,

        DeliveryRequestDto delivery,

        PaymentRequestDto payment,

        Integer voucherId,

        Integer customerId,

        List<Integer> cartItemIds
) {
}
