package com.project.marketplace.controller.customer;

import com.project.marketplace.dto.*;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/customer/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{cartId}")
    public ApiResponse<CartResponseDto> getCartById(@PathVariable("cartId") Integer cartId) {
        CartResponseDto cart = cartService.getCartById(cartId);
        return ApiResponse.success(cart);
    }

    @PostMapping
    public ApiResponse<CartResponseDto> addCart(@RequestBody CartRequestDto dto) {
        CartResponseDto newCart = cartService.addCart(dto);
        return ApiResponse.success(newCart);
    }

    @PutMapping("/{cartId}")
    public  ApiResponse<CartResponseDto> updateCart(@RequestBody CartRequestDto dto, @PathVariable("cartId") Integer cartId) {
        CartResponseDto updatedCart = cartService.updateCart(dto, cartId);
        return ApiResponse.success(updatedCart);
    }

    @DeleteMapping("/{cartId}")
    public ApiResponse<Void> deleteCart(@PathVariable("cartId") Integer cartId) {
        cartService.deleteCart(cartId);
        return ApiResponse.success();
    }

    @GetMapping("/deliveryFee/{cartId}")
    public ApiResponse<BigDecimal> calculateDeliveryFee(@PathVariable("cartId") Integer cartId, @RequestParam("deliveries") List<String> deliveries) {
        BigDecimal deliveryFee = cartService.calculateDeliveryFee(cartId, deliveries);
        return ApiResponse.success(deliveryFee);
    }


    @PatchMapping("/addToCart")
    public ApiResponse<CartResponseDto> addProductToCart(@RequestBody CartItemRequestDto dto) {
        CartResponseDto updateCart = cartService.addProductToCart(dto);
        return ApiResponse.success(updateCart);
    }

    @DeleteMapping("/deleteFromCart/{cartItemId}/{cartId}")
    public ApiResponse<CartResponseDto> deleteProductFromCart(@PathVariable("cartItemId") Integer productId, @PathVariable("cartId") Integer cartId) {
        CartResponseDto updateCart = cartService.deleteProductFromCart(productId, cartId);
        return ApiResponse.success(updateCart);
    }

    @DeleteMapping("/deleteAllFromCart/{cartId}")
    public ApiResponse<CartResponseDto> deleteAllProductsFromCart(@PathVariable("cartId") Integer cartId) {
        CartResponseDto updateCart = cartService.deleteAllProductsFromCart(cartId);
        return ApiResponse.success(updateCart);
    }

    @PatchMapping("/updateQuantity/{quantity}/{cartItemId}")
    public ApiResponse<CartResponseDto> updateCartItemQuantity(@PathVariable("quantity") int quantity, @PathVariable("cartItemId") Integer cartItemId) {
        CartResponseDto updatedCart = cartService.updateCartItemQuantity(quantity, cartItemId);
        return ApiResponse.success(updatedCart);
    }

    @PostMapping("/checkout/{cartId}")
    public ApiResponse<List<OrderResponseDto>> checkout(@PathVariable("cartId") Integer cartId, @RequestBody List<OrderRequestDto> dtos) {
        List<OrderResponseDto> orders = cartService.checkout(dtos, cartId);
        return  ApiResponse.success(orders);
    }
}
