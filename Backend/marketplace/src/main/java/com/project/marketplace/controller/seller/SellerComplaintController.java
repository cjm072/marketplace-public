package com.project.marketplace.controller.seller;

import com.project.marketplace.dto.ComplaintResponseDto;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.ComplaintService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seller/complaint")
public class SellerComplaintController {

    private final ComplaintService complaintService;

    public SellerComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping("/batch/{sellerId}")
    public ApiResponse<List<ComplaintResponseDto>> getAllComplaintsBySeller(@PathVariable("sellerId") Integer sellerId){
        List<ComplaintResponseDto> complaints = complaintService.getAllComplaintsBySeller(sellerId);
        return ApiResponse.success(complaints);
    }
}
