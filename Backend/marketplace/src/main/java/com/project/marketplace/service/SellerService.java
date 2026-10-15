package com.project.marketplace.service;

import com.project.marketplace.dto.SellerRequestDto;
import com.project.marketplace.dto.SellerResponseDto;

import java.util.List;

public interface SellerService {
    SellerResponseDto getSellerById (Integer sellerId);
    List<SellerResponseDto> getAllPendingSellers ();
    SellerResponseDto addSeller (SellerRequestDto dto);
    SellerResponseDto updateSeller (SellerRequestDto dto, Integer sellerId);
    void deleteSeller (Integer sellerId);
    SellerResponseDto approveSeller (Integer sellerId);
    SellerResponseDto rejectSeller (Integer sellerId);
    void register(SellerRequestDto dto);
    boolean checkSellerStatus(Integer sellerId);
}
