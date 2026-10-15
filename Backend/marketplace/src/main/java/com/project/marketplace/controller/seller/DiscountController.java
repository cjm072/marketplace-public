package com.project.marketplace.controller.seller;

import com.project.marketplace.dto.DiscountRequestDto;
import com.project.marketplace.dto.DiscountResponseDto;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.DiscountService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/seller/discount")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping("/{discountId}")
    public ApiResponse<DiscountResponseDto> getDiscountById(@PathVariable("discountId") Integer discountId){
        DiscountResponseDto discount = discountService.getDiscountById(discountId);
        return ApiResponse.success(discount);
    }

    @GetMapping("/batch/{sellerId}")
    public ApiResponse<List<DiscountResponseDto>> getAllDiscountsBySeller(@PathVariable("sellerId") Integer sellerId){
        List<DiscountResponseDto> discounts = discountService.getAllDiscountsBySeller(sellerId);
        return ApiResponse.success(discounts);
    }

    @PostMapping
    public ApiResponse<DiscountResponseDto> addDiscount(@Validated @RequestBody DiscountRequestDto dto) {
        DiscountResponseDto newDiscount = discountService.addDiscount(dto);
        return ApiResponse.success(newDiscount);
    }

    @PutMapping("/{discountId}")
    public ApiResponse<DiscountResponseDto> updateDiscount(@Validated @RequestBody DiscountRequestDto dto, @PathVariable("discountId") Integer discountId) {
        DiscountResponseDto updatedDiscount = discountService.updateDiscount(dto, discountId);
        return ApiResponse.success(updatedDiscount);
    }

    @DeleteMapping("/{discountId}")
    public ApiResponse<Void> deleteDiscount(@PathVariable("discountId") Integer discountId) {
        discountService.deleteDiscount(discountId);
        return ApiResponse.success();
    }
}
