package com.project.marketplace.service;

import com.project.marketplace.dto.VoucherRequestDto;
import com.project.marketplace.dto.VoucherResponseDto;
import com.project.marketplace.mapper.VoucherMapper;
import com.project.marketplace.pojo.Voucher;
import com.project.marketplace.repository.VoucherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VoucherServiceImpl implements VoucherService{

    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;

    public VoucherServiceImpl(VoucherRepository voucherRepository, VoucherMapper voucherMapper) {
        this.voucherRepository = voucherRepository;
        this.voucherMapper = voucherMapper;
    }

    @Override
    public List<VoucherResponseDto> getAllVouchers(){
        List<Voucher> vouchers = voucherRepository.findAll();
        return voucherMapper.toVoucherResponseDtoList(vouchers);
    }

    @Override
    public VoucherResponseDto addVoucher(VoucherRequestDto dto){
        Voucher voucher = voucherMapper.toVoucher(dto);
        Voucher savedVoucher = voucherRepository.save(voucher);
        return voucherMapper.toVoucherResponseDto(savedVoucher);
    }

    @Override
    public VoucherResponseDto updateVoucher(VoucherRequestDto dto, Integer voucherId){
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new NoSuchElementException("Voucher with ID " + voucherId + " not found"));
        voucherMapper.updateVoucherFromDto(dto, voucher);
        Voucher savedVoucher = voucherRepository.save(voucher);
        return voucherMapper.toVoucherResponseDto(savedVoucher);
    }

    @Override
    public void deleteVoucher(Integer voucherId){
        voucherRepository.deleteById(voucherId);
    }
}