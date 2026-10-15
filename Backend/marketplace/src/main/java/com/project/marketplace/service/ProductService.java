package com.project.marketplace.service;

import com.project.marketplace.dto.ProductRequestDto;
import com.project.marketplace.dto.ProductResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductResponseDto> getAllProducts();
    List<ProductResponseDto> getAllApprovedProducts();
    ProductResponseDto getProductById(Integer productId);
    List<ProductResponseDto> getProductsByName(String name);
    List<ProductResponseDto> getAllProductsBySeller(Integer sellerId);
    List<ProductResponseDto> getAllPendingProducts();
    ProductResponseDto addProduct(ProductRequestDto dto);
    ProductResponseDto updateProduct(ProductRequestDto dto, Integer productId);
    void deleteProduct(Integer productId);
    ProductResponseDto applyDiscountToProduct(Integer productId, Integer discountId);
    ProductResponseDto removeDiscountFromProduct(Integer productId, Integer discountId);
    ProductResponseDto approveProduct(Integer productId);
    ProductResponseDto rejectProduct(Integer productId);
    void uploadProductPicture(MultipartFile file, Integer productId);
}
