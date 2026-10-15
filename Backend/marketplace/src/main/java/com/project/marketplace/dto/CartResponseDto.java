package com.project.marketplace.dto;

import java.util.List;

public record CartResponseDto(

        Integer id,

        Integer customerId,

        List<CartItemResponseDto> cartItems
) {
}
