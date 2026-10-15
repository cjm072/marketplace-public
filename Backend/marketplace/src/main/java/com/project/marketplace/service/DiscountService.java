package com.project.marketplace.service;

import com.project.marketplace.dto.DiscountRequestDto;
import com.project.marketplace.dto.DiscountResponseDto;

import java.util.List;

public interface DiscountService {
    DiscountResponseDto getDiscountById(Integer discountId);
    List<DiscountResponseDto> getAllDiscountsBySeller(Integer sellerId);
    DiscountResponseDto addDiscount(DiscountRequestDto dto);
    DiscountResponseDto updateDiscount(DiscountRequestDto dto, Integer discountId);
    void deleteDiscount(Integer discountId);
}
