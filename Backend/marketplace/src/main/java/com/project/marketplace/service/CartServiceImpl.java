package com.project.marketplace.service;

import com.project.marketplace.dto.*;
import com.project.marketplace.mapper.CartMapper;
import com.project.marketplace.pojo.Cart;
import com.project.marketplace.pojo.CartItem;
import com.project.marketplace.repository.CartItemRepository;
import com.project.marketplace.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final CartItemService cartItemService;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    public CartServiceImpl(CartRepository cartRepository, CartMapper cartMapper, CartItemService cartItemService, CartItemRepository cartItemRepository, OrderService orderService) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.cartItemService = cartItemService;
        this.cartItemRepository = cartItemRepository;
        this.orderService = orderService;
    }

    @Transactional
    @Override
    public CartResponseDto getCartById(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NoSuchElementException("Cart with ID " + cartId + " not found"));
        List<CartItem> cartItems = cart.getCartItems();
        List<CartItem> itemsToRemove = cartItems.stream()
                .filter(ci -> ci.getOrder() != null)
                .toList();
        cartItems.removeAll(itemsToRemove);
        cartItemRepository.deleteAll(itemsToRemove);
        return cartMapper.toCartResponseDto(cart);
    }


    @Override
    public CartResponseDto addCart(CartRequestDto dto) {
        Optional<Cart> existingCartOpt = cartRepository.findByCustomerId(dto.customerId());
        if (existingCartOpt.isPresent()) {
            return cartMapper.toCartResponseDto(existingCartOpt.get());
        }
        Cart cart = cartMapper.toCart(dto);
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toCartResponseDto(savedCart);
    }

    @Override
    public CartResponseDto updateCart(CartRequestDto dto, Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NoSuchElementException("Cart with ID " + cartId + " not found"));
        cartMapper.updateCartFromDto(dto, cart);
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toCartResponseDto(savedCart);
    }

    @Override
    public void deleteCart(Integer cartId) {
        cartRepository.deleteById(cartId);
    }

    @Override
    public BigDecimal calculateDeliveryFee(Integer cartId, List<String> deliveries) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NoSuchElementException("Cart with ID " + cartId + " not found"));
        List<CartItem> cartItems = cart.getCartItems();
        Map<Integer, List<CartItem>> itemsBySeller = new HashMap<>();
        for (CartItem item : cartItems) {
            Integer sellerId = item.getProduct().getSeller().getId();
            itemsBySeller.computeIfAbsent(sellerId, k -> new ArrayList<>()).add(item);
        }
        BigDecimal totalFee = BigDecimal.ZERO;
        int i = 0;
        for (Integer sellerId : itemsBySeller.keySet()) {
            if (i >= deliveries.size()) {
                throw new IllegalArgumentException("Not enough delivery options provided.");
            }
            String method = deliveries.get(i).toLowerCase();
            switch (method) {
                case "standard":
                    totalFee = totalFee.add(BigDecimal.valueOf(1.00));
                    break;
                case "express":
                    totalFee = totalFee.add(BigDecimal.valueOf(3.00));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid delivery method: " + method);
            }
            i++;
        }
        return totalFee;
    }

    @Transactional
    @Override
    public CartResponseDto addProductToCart(CartItemRequestDto dto) {
        Cart cart = cartRepository.findById(dto.cartId())
                .orElseThrow(() -> new NoSuchElementException("Cart not found"));
        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems == null) {
            cartItems = new ArrayList<>();
            cart.setCartItems(cartItems);
        }
        Optional<CartItem> existingItemOpt = cartItems.stream()
                .filter(item -> item.getProduct().getId().equals(dto.productId()))
                .findFirst();
        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + dto.quantity());
        } else {
            CartItem newItem = cartItemService.addCartItem(dto);
            cartItems.add(newItem);
        }
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toCartResponseDto(savedCart);
    }


    @Transactional
    @Override
    public CartResponseDto deleteProductFromCart(Integer cartItemId, Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NoSuchElementException("Cart with ID " + cartId + " not found"));
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NoSuchElementException("CartItem with ID " + cartItemId + " not found"));
        cartItemService.deleteCartItem(cartItem.getId());
        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems != null && cartItems.contains(cartItem)) {
            cartItems.remove(cartItem);
        } else {
            throw new NoSuchElementException("CartItem with ID " + cartItem.getId() + " not found in cart " + cart.getId());
        }
        cart.setCartItems(cartItems);
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toCartResponseDto(savedCart);
    }

    @Transactional
    @Override
    public CartResponseDto deleteAllProductsFromCart(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NoSuchElementException("Cart with ID " + cartId + " not found"));
        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems != null && !cartItems.isEmpty()) {
            for (CartItem item : cartItems) {
                cartItemService.deleteCartItem(item.getId());
            }
            cartItems.clear();
        }
        cart.setCartItems(cartItems);
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toCartResponseDto(savedCart);
    }

    @Transactional
    @Override
    public CartResponseDto updateCartItemQuantity(int quantity, Integer cartItemId) {
        CartItem cartItem = cartItemService.updateQuantity(quantity, cartItemId);
        Cart cart = cartItem.getCart();
        return cartMapper.toCartResponseDto(cart);
    }

    @Transactional
    @Override
    public List<OrderResponseDto> checkout(List<OrderRequestDto> dtos, Integer cartId) {
        List<OrderResponseDto> orders = new ArrayList<>(dtos.size());
        for (OrderRequestDto dto : dtos) {
            orders.add(orderService.addOrder(dto));
        }
        cartRepository.deleteById(cartId);
        return orders;
    }
}
