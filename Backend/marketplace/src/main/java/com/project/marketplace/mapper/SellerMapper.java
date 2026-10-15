package com.project.marketplace.mapper;

import com.project.marketplace.dto.SellerRequestDto;
import com.project.marketplace.dto.SellerResponseDto;
import com.project.marketplace.pojo.Seller;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SellerMapper {
    public Seller toSeller(SellerRequestDto dto){
        if ( dto == null ) {
            return null;
        }
        Seller seller = new Seller();
        updateSellerFromDto(dto, seller);
        return seller;
    }

    public void updateSellerFromDto(SellerRequestDto dto, Seller seller){
        seller.setUsername(dto.getUsername());
        seller.setPassword(dto.getPassword());
        seller.setEmail(dto.getEmail());
        seller.setPhone(dto.getPhone());
        seller.setRole(dto.getRole());
        seller.setCompany(dto.getCompany());
    }

    public SellerResponseDto toSellerResponseDto(Seller seller){
        if ( seller == null ) {
            return null;
        }

        return new SellerResponseDto(
                seller.getId(),
                seller.getUsername(),
                seller.getEmail(),
                seller.getPhone(),
                seller.getImage(),
                seller.getAddress(),
                seller.getCompany()
        );
    }

    public List<SellerResponseDto> toSellerResponseDtoList(List<Seller> sellers){
        if ( sellers == null ) {
            return null;
        }
        List<SellerResponseDto> list = new ArrayList<>( sellers.size() );
        for ( Seller seller : sellers ) {
            list.add( toSellerResponseDto(seller) );
        }

        return list;
    }
}
