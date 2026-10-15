package com.project.marketplace.mapper;

import com.project.marketplace.dto.VoucherRequestDto;
import com.project.marketplace.dto.VoucherResponseDto;
import com.project.marketplace.pojo.Voucher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VoucherMapper {
    public Voucher toVoucher(VoucherRequestDto dto){
        if ( dto == null ) {
            return null;
        }
        Voucher voucher = new Voucher();
        updateVoucherFromDto(dto, voucher);
        return voucher;
    }

    public void updateVoucherFromDto(VoucherRequestDto dto, Voucher voucher){
        voucher.setName(dto.name());
        voucher.setDescription(dto.description());
        voucher.setStartDate(dto.startDate());
        voucher.setExpiryDate(dto.expiryDate());
        voucher.setAmount(dto.amount());
        voucher.setCategory(dto.category());
        voucher.setQuantity(dto.quantity());
    }

    public VoucherResponseDto toVoucherResponseDto(Voucher voucher){
        if ( voucher == null ) {
            return null;
        }

        return new VoucherResponseDto(
                voucher.getId(),
                voucher.getName(),
                voucher.getDescription(),
                voucher.getStartDate(),
                voucher.getExpiryDate(),
                voucher.getAmount(),
                voucher.getCategory(),
                voucher.getQuantity()
        );
    }

    public List<VoucherResponseDto> toVoucherResponseDtoList(List<Voucher> vouchers){
        if ( vouchers == null ) {
            return null;
        }
        List<VoucherResponseDto> list = new ArrayList<>( vouchers.size() );
        for ( Voucher voucher : vouchers ) {
            list.add( toVoucherResponseDto( voucher ) );
        }

        return list;
    }
}