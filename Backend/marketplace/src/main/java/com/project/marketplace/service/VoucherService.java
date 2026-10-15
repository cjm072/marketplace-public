package com.project.marketplace.service;

import com.project.marketplace.dto.VoucherRequestDto;
import com.project.marketplace.dto.VoucherResponseDto;

import java.util.List;

public interface VoucherService {

    List<VoucherResponseDto> getAllVouchers();
    VoucherResponseDto addVoucher(VoucherRequestDto voucherRequestDto);
    VoucherResponseDto updateVoucher(VoucherRequestDto voucherRequestDto, Integer voucherId);
    void deleteVoucher(Integer voucherId);
}
