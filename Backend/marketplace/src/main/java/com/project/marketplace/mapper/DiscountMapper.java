package com.project.marketplace.mapper;

import com.project.marketplace.dto.DiscountRequestDto;
import com.project.marketplace.dto.DiscountResponseDto;
import com.project.marketplace.pojo.Discount;
import com.project.marketplace.pojo.Seller;
import com.project.marketplace.repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DiscountMapper {
    private final SellerRepository sellerRepository;

    public DiscountMapper(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public Discount toDiscount(DiscountRequestDto dto){
        if ( dto == null ) {
            return null;
        }
        Discount discount = new Discount();

        updateDiscountFromDto(dto, discount);

        return discount;
    }

    public void updateDiscountFromDto(DiscountRequestDto dto, Discount discount){
        Seller seller = sellerRepository.findById(dto.sellerId())
                        .orElseThrow(() -> new NoSuchElementException("Seller with ID " + dto.sellerId() + " not found"));
        discount.setName(dto.name());
        discount.setDescription(dto.description());
        discount.setAmount(dto.amount());
        discount.setStartDate(dto.startDate());
        discount.setExpiryDate(dto.expiryDate());
        discount.setSeller(seller);
    }

    public DiscountResponseDto toDiscountResponseDto(Discount discount){
        if ( discount == null ) {
            return null;
        }

        return new DiscountResponseDto(
                discount.getId(),
                discount.getName(),
                discount.getDescription(),
                discount.getAmount(),
                discount.getStartDate(),
                discount.getExpiryDate(),
                discount.getSeller().getId()
        );
    }

    public List<DiscountResponseDto> toDiscountResponseDtoList(List<Discount> discounts){
        if ( discounts == null ) {
            return null;
        }
        List<DiscountResponseDto> list = new ArrayList<>( discounts.size() );
        for ( Discount discount : discounts ) {
            list.add( toDiscountResponseDto( discount ));
        }

        return list;
    }
}
