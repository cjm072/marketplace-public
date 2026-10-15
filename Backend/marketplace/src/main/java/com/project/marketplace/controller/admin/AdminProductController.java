package com.project.marketplace.controller.admin;

import com.project.marketplace.dto.ProductResponseDto;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/admin/product")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ApiResponse<List<ProductResponseDto>> getAllPendingProducts(){
        List<ProductResponseDto> pendingProducts = productService.getAllPendingProducts();
        return ApiResponse.success(pendingProducts);
    }

    @PatchMapping("/approve/{productId}")
    public ApiResponse<ProductResponseDto> approveProduct(@PathVariable("productId") Integer productId){
        ProductResponseDto updatedProduct = productService.approveProduct(productId);
        return ApiResponse.success(updatedProduct);
    }

    @PatchMapping("/reject/{productId}")
    public ApiResponse<ProductResponseDto> rejectProduct(@PathVariable("productId") Integer productId){
        ProductResponseDto updatedProduct = productService.rejectProduct(productId);
        return ApiResponse.success(updatedProduct);
    }
}
