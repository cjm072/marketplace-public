package com.project.marketplace.service;

import com.project.marketplace.dto.*;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {
    CartResponseDto getCartById(Integer cartId);
    CartResponseDto addCart(CartRequestDto cartRequestDto);
    CartResponseDto updateCart(CartRequestDto cartRequestDto, Integer cartId);
    void deleteCart(Integer cartId);
    CartResponseDto deleteAllProductsFromCart(Integer cartId);
    BigDecimal calculateDeliveryFee(Integer cartId, List<String> deliveries);
    CartResponseDto addProductToCart(CartItemRequestDto dto);
    CartResponseDto deleteProductFromCart(Integer cartItemId, Integer cartId);
    CartResponseDto updateCartItemQuantity(int quantity, Integer cartItemId);
    List<OrderResponseDto> checkout(List<OrderRequestDto> dtos, Integer cartId);
}
