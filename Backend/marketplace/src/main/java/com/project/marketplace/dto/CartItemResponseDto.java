package com.project.marketplace.dto;

import com.project.marketplace.pojo.ProductCategory;

import java.math.BigDecimal;

public record CartItemResponseDto(

        Integer id,

        Integer cartId,

        String productName,

        ProductCategory productCategory,

        int quantity,

        String productImage,

        int co2,

        BigDecimal unitPrice,

        BigDecimal subtotal,

        Integer sellerId,

        String sellerName
) {
}
