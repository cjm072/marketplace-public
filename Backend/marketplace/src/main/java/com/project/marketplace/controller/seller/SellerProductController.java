package com.project.marketplace.controller.seller;

import com.project.marketplace.dto.ProductRequestDto;
import com.project.marketplace.dto.ProductResponseDto;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.ProductService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/seller/product")
public class SellerProductController {

    private final ProductService productService;

    public SellerProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponseDto> getProductById(@PathVariable("productId") Integer productId) {
        ProductResponseDto product = productService.getProductById(productId);
        return ApiResponse.success(product);
    }

    @GetMapping("/batch/{sellerId}")
    public ApiResponse<List<ProductResponseDto>> getAllProductsBySeller(@PathVariable("sellerId") Integer sellerId) {
        List<ProductResponseDto> products = productService.getAllProductsBySeller(sellerId);
        return ApiResponse.success(products);
    }

    @PostMapping
    public ApiResponse<ProductResponseDto> addProduct(@Validated @RequestBody ProductRequestDto dto) {
        ProductResponseDto newProduct = productService.addProduct(dto);
        return ApiResponse.success(newProduct);
    }

    @PutMapping("/{productId}")
    public ApiResponse<ProductResponseDto> updateProduct(@Validated @RequestBody ProductRequestDto dto, @PathVariable("productId") Integer productId) {
        ProductResponseDto updatedProduct = productService.updateProduct(dto, productId);
        return ApiResponse.success(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<Void> deleteProduct(@PathVariable("productId") Integer productId) {
        productService.deleteProduct(productId);
        return ApiResponse.success();
    }

    @PutMapping("/{productId}/applyDiscount/{discountId}")
    public ApiResponse<ProductResponseDto> applyDiscountToProduct(@PathVariable("productId") Integer productId ,@PathVariable("discountId") Integer discountId){
        ProductResponseDto updatedProduct = productService.applyDiscountToProduct(productId, discountId);
        return ApiResponse.success(updatedProduct);
    }

    @PutMapping("/{productId}/removeDiscount/{discountId}")
    public ApiResponse<ProductResponseDto> removeDiscountFromProduct(@PathVariable("productId") Integer productId ,@PathVariable("discountId") Integer discountId){
        ProductResponseDto updatedProduct = productService.removeDiscountFromProduct(productId, discountId);
        return ApiResponse.success(updatedProduct);
    }

    @PutMapping("/uploadProductPicture")
    public ApiResponse<Void> uploadProductPicture(@RequestParam("image") MultipartFile file, @RequestParam("productId") Integer productId) {
        productService.uploadProductPicture(file, productId);
        return ApiResponse.success();
    }
}
