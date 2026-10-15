package com.project.marketplace.dto;

public record CartItemRequestDto(

        Integer cartId,

        Integer productId,

        int quantity
) {
}
