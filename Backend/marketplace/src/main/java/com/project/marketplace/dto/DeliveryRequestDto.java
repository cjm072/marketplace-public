package com.project.marketplace.dto;

import com.project.marketplace.pojo.Address;
import com.project.marketplace.pojo.DeliveryMethod;

public record DeliveryRequestDto(

        DeliveryMethod method,

        String courier,

        Address shippingAddress
) {
}
