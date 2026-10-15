package com.project.marketplace.controller.admin;

import com.project.marketplace.dto.SellerResponseDto;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.SellerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/admin/account")
public class AdminAccountController {

    private final SellerService sellerService;

    public AdminAccountController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @GetMapping
    public ApiResponse<List<SellerResponseDto>> getAllPendingSellers(){
        List<SellerResponseDto> sellers = sellerService.getAllPendingSellers();
        return ApiResponse.success(sellers);
    }

    @PatchMapping("/approve/{sellerId}")
    public ApiResponse<SellerResponseDto> approveSeller(@PathVariable("sellerId") Integer sellerId) {
        SellerResponseDto updatedSeller = sellerService.approveSeller(sellerId);
        return ApiResponse.success(updatedSeller);
    }

    @PatchMapping("/reject/{sellerId}")
    public ApiResponse<SellerResponseDto> rejectSeller(@PathVariable("sellerId") Integer sellerId) {
        SellerResponseDto updatedSeller = sellerService.rejectSeller(sellerId);
        return ApiResponse.success(updatedSeller);
    }
}
