package com.project.marketplace.service;

import com.project.marketplace.dto.CartItemRequestDto;
import com.project.marketplace.pojo.CartItem;

public interface CartItemService {
    CartItem addCartItem(CartItemRequestDto dto);
    void deleteCartItem(Integer cartItemId);
    CartItem updateQuantity(int quantity, Integer cartItemId);
}
