package com.project.marketplace.mapper;

import com.project.marketplace.dto.CartItemRequestDto;
import com.project.marketplace.dto.CartItemResponseDto;
import com.project.marketplace.pojo.Cart;
import com.project.marketplace.pojo.CartItem;
import com.project.marketplace.pojo.Product;
import com.project.marketplace.repository.CartRepository;
import com.project.marketplace.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CartItemMapper {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartItemMapper(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public CartItem toCart(CartItemRequestDto dto) {
        if ( dto == null ) {
            return  null;
        }
        CartItem cartItem = new CartItem();

        updateCartItemFromDto(dto, cartItem);

        return cartItem;
    }

    public void updateCartItemFromDto(CartItemRequestDto dto, CartItem cartItem) {
        Cart cart = cartRepository.findById(dto.cartId())
                .orElseThrow(() -> new NoSuchElementException("Cart with ID " + dto.cartId() + " not found"));
        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new NoSuchElementException("Product with ID " + dto.productId() + " not found"));
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(dto.quantity());
        //cartItem.setUnitPrice(dto.unitPrice());
    }

    public CartItemResponseDto toCartItemResponseDto(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }

        return new CartItemResponseDto(
                cartItem.getId(),
                cartItem.getCart().getId(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getCategory(),
                cartItem.getQuantity(),
                cartItem.getProduct().getImage(),
                cartItem.getProduct().getCo2(),
                cartItem.getUnitPrice(),
                cartItem.getSubtotal(),
                cartItem.getProduct().getSeller().getId(),
                cartItem.getProduct().getSeller().getUsername()
        );
    }

    public List<CartItemResponseDto> toCartItemResponseDtoList(List<CartItem> cartItems) {
        if ( cartItems == null ) {
            return null;
        }
        List<CartItemResponseDto> list = new ArrayList<>( cartItems.size() );
        for ( CartItem cartItem : cartItems ) {
            list.add( toCartItemResponseDto(cartItem) );
        }

        return list;
    }
}
