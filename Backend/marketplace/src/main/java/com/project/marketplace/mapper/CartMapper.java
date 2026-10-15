package com.project.marketplace.mapper;

import com.project.marketplace.dto.CartItemResponseDto;
import com.project.marketplace.dto.CartRequestDto;
import com.project.marketplace.dto.CartResponseDto;
import com.project.marketplace.pojo.Cart;
import com.project.marketplace.pojo.Customer;
import com.project.marketplace.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CartMapper {

    private final CustomerRepository customerRepository;
    private final CartItemMapper cartItemMapper;

    public CartMapper(CustomerRepository customerRepository, CartItemMapper cartItemMapper) {
        this.customerRepository = customerRepository;
        this.cartItemMapper = cartItemMapper;
    }

    public Cart toCart(CartRequestDto dto) {
        if (dto == null) {
            return null;
        }
        Cart cart = new Cart();

        updateCartFromDto(dto, cart);

        return cart;
    }

    public void updateCartFromDto(CartRequestDto dto, Cart cart) {
        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new NoSuchElementException("Customer with ID " + dto.customerId() + " not found"));
//        List<Product> products = productRepository.findAllById(dto.productIds());
//        if (products.size() != dto.productIds().size()) {
//            throw new NoSuchElementException("One or more products not found for provided IDs: " + dto.productIds());
//        }
        cart.setCustomer(customer);
//        cart.setProducts(products);
    }

    public CartResponseDto toCartResponseDto(Cart cart) {
        if (cart == null) {
            return null;
        }

        List<CartItemResponseDto> cartItemResponseDtos = cartItemMapper.toCartItemResponseDtoList(cart.getCartItems());

        return new CartResponseDto(
                cart.getId(),
                cart.getCustomer().getId(),
                cartItemResponseDtos
        );
    }

    public List<CartResponseDto> toCartResponseDtoList(List<Cart> carts) {
        if (carts == null) {
            return null;
        }
        List<CartResponseDto> list = new ArrayList<>( carts.size() );
        for ( Cart cart : carts ) {
            list.add( toCartResponseDto(cart) );
        }

        return list;
    }
}
