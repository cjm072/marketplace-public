package com.project.marketplace.controller.seller;

import com.project.marketplace.dto.SellerRequestDto;
import com.project.marketplace.dto.SellerResponseDto;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.SellerService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/seller/account")
public class SellerAccountController {

    private final SellerService sellerService;

    public SellerAccountController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @GetMapping("/{sellerId}")
    public ApiResponse<SellerResponseDto> getSellerById(@PathVariable("sellerId") Integer sellerId){
        SellerResponseDto seller = sellerService.getSellerById(sellerId);
        return ApiResponse.success(seller);
    }

    @PostMapping
    public ApiResponse<SellerResponseDto> addSeller(@RequestBody SellerRequestDto dto){
        SellerResponseDto newSeller = sellerService.addSeller(dto);
        return ApiResponse.success(newSeller);
    }

    @PutMapping("/{sellerId}")
    public ApiResponse<SellerResponseDto> updateSeller(@RequestBody SellerRequestDto dto, @PathVariable("sellerId") Integer sellerId){
        SellerResponseDto updatedSeller = sellerService.updateSeller(dto, sellerId);
        return ApiResponse.success(updatedSeller);
    }

    @DeleteMapping("/{sellerId}")
    public ApiResponse<Void> deleteSeller(@PathVariable("sellerId") Integer sellerId){
        sellerService.deleteSeller(sellerId);
        return ApiResponse.success();
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody SellerRequestDto dto) {
        sellerService.register(dto);
        return ApiResponse.success();
    }
}
