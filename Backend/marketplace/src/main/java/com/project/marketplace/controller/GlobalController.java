package com.project.marketplace.controller;

import com.project.marketplace.dto.ProductResponseDto;
import com.project.marketplace.dto.VoucherResponseDto;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.ProductService;
import com.project.marketplace.service.VoucherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/global")
public class GlobalController {

    private final ProductService productService;
    private final VoucherService voucherService;

    public GlobalController(ProductService productService, VoucherService voucherService) {
        this.productService = productService;
        this.voucherService = voucherService;
    }

    @GetMapping("/product/{productId}")
    public ApiResponse<ProductResponseDto> getProductById(@PathVariable("productId") Integer id) {
        ProductResponseDto product = productService.getProductById(id);
        return ApiResponse.success(product);
    }

    @GetMapping("/voucher/batch")
    public ApiResponse<List<VoucherResponseDto>> getAllVouchers() {
        List<VoucherResponseDto> vouchers = voucherService.getAllVouchers();
        return ApiResponse.success(vouchers);
    }

    @GetMapping("/product/searchByName")
    public ApiResponse<List<ProductResponseDto>> getProductsByName(@RequestParam String name) {
        List<ProductResponseDto> products = productService.getProductsByName(name);
        return ApiResponse.success(products);
    }
}
