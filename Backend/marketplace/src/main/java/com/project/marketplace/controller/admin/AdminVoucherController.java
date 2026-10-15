package com.project.marketplace.controller.admin;

import com.project.marketplace.dto.VoucherRequestDto;
import com.project.marketplace.dto.VoucherResponseDto;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.VoucherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/admin/voucher")
public class AdminVoucherController {

    private final VoucherService voucherService;

    public AdminVoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping("/batch")
    public ApiResponse<List<VoucherResponseDto>> getAllVouchers() {
        List<VoucherResponseDto> vouchers = voucherService.getAllVouchers();
        return ApiResponse.success(vouchers);
    }

    @PostMapping
    public ApiResponse<VoucherResponseDto> addVoucher(@RequestBody VoucherRequestDto dto) {
        VoucherResponseDto newVoucher = voucherService.addVoucher(dto);
        return ApiResponse.success(newVoucher);
    }

    @PutMapping("/{voucherId}")
    public ApiResponse<VoucherResponseDto> updateVoucher(@RequestBody VoucherRequestDto dto, @PathVariable("voucherId") Integer voucherId) {
        VoucherResponseDto updatedVoucher = voucherService.updateVoucher(dto, voucherId);
        return ApiResponse.success(updatedVoucher);
    }

    @DeleteMapping("/{voucherId}")
    public ApiResponse<Void> deleteVoucher(@PathVariable("voucherId") Integer voucherId) {
        voucherService.deleteVoucher(voucherId);
        return ApiResponse.success();
    }
}
