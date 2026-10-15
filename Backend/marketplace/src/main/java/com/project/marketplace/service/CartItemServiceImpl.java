package com.project.marketplace.service;

import com.project.marketplace.dto.CartItemRequestDto;
import com.project.marketplace.mapper.CartItemMapper;
import com.project.marketplace.pojo.CartItem;
import com.project.marketplace.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, CartItemMapper cartItemMapper) {
        this.cartItemRepository = cartItemRepository;
        this.cartItemMapper = cartItemMapper;
    }

    @Override
    public CartItem addCartItem(CartItemRequestDto dto) {
        CartItem cartItem = cartItemMapper.toCart(dto);
        cartItem.setUnitPrice(cartItem.getProduct().getPrice());
        return cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteCartItem(Integer cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public CartItem updateQuantity(int quantity, Integer cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NoSuchElementException("CartItem with ID " + cartItemId + " not found"));
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }
}
