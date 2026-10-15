package com.project.marketplace.controller.customer;

import com.project.marketplace.dto.ProductResponseDto;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/customer/product")
public class CustomerProductController {

    private final ProductService productService;

    public CustomerProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/batch")
    public ApiResponse<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();
        return ApiResponse.success(products);
    }

    @GetMapping("/batch/approvedProducts")
    public ApiResponse<List<ProductResponseDto>> getAllApprovedProducts() {
        List<ProductResponseDto> products = productService.getAllApprovedProducts();
        return ApiResponse.success(products);
    }

    @GetMapping("/searchByName")
    public ApiResponse<List<ProductResponseDto>> getProductsByName(@RequestParam String name) {
        List<ProductResponseDto> products = productService.getProductsByName(name);
        return ApiResponse.success(products);
    }

}
