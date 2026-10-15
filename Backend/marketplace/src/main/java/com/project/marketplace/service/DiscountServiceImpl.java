package com.project.marketplace.service;

import com.project.marketplace.dto.DiscountRequestDto;
import com.project.marketplace.dto.DiscountResponseDto;
import com.project.marketplace.mapper.DiscountMapper;
import com.project.marketplace.pojo.Discount;
import com.project.marketplace.pojo.Product;
import com.project.marketplace.pojo.Seller;
import com.project.marketplace.repository.DiscountRepository;
import com.project.marketplace.repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DiscountServiceImpl implements DiscountService{

    private final DiscountRepository discountRepository;
    private final DiscountMapper discountMapper;
    private final SellerRepository sellerRepository;

    public DiscountServiceImpl(DiscountRepository discountRepository, DiscountMapper discountMapper, SellerRepository sellerRepository) {
        this.discountRepository = discountRepository;
        this.discountMapper = discountMapper;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public DiscountResponseDto getDiscountById(Integer discountId){
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new NoSuchElementException("Discount with ID " + discountId + " not found"));
        return discountMapper.toDiscountResponseDto(discount);
    }

    @Override
    public List<DiscountResponseDto> getAllDiscountsBySeller(Integer sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new NoSuchElementException("Seller with ID " + sellerId + " not found"));
        List<Discount> discounts = discountRepository.findAllBySeller(seller);
        return discountMapper.toDiscountResponseDtoList(discounts);
    }

    @Override
    public DiscountResponseDto addDiscount(DiscountRequestDto dto){
        Discount discount = discountMapper.toDiscount(dto);
        Discount savedDiscount = discountRepository.save(discount);
        return discountMapper.toDiscountResponseDto(savedDiscount);
    }

    @Override
    public DiscountResponseDto updateDiscount(DiscountRequestDto dto, Integer discountId){
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new NoSuchElementException("Discount with ID " + discountId + " not found"));
        discountMapper.updateDiscountFromDto(dto, discount);
        Discount savedDiscount = discountRepository.save(discount);
        return discountMapper.toDiscountResponseDto(savedDiscount);
    }

    @Override
    public void deleteDiscount(Integer discountId){
        discountRepository.deleteById(discountId);
    }
}
