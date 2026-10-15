package com.project.marketplace.controller.customer;

import com.project.marketplace.dto.ComplaintRequestDto;
import com.project.marketplace.dto.ComplaintResponseDto;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.ComplaintService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/customer/complaint")
public class CustomerComplaintController {

    private final ComplaintService complaintService;

    public CustomerComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping("/batch/{customerId}")
    public ApiResponse<List<ComplaintResponseDto>> getAllComplaintsByCustomer(@PathVariable("customerId") Integer customerId){
        List<ComplaintResponseDto> complaints = complaintService.getAllComplaintsByCustomer(customerId);
        return ApiResponse.success(complaints);
    }

    @PostMapping
    public ApiResponse<ComplaintResponseDto> addComplaint(@RequestBody ComplaintRequestDto dto){
        ComplaintResponseDto newComplaint = complaintService.addComplaint(dto);
        return ApiResponse.success(newComplaint);
    }

    @PatchMapping("/complaint/{complaintId}")
    public ApiResponse<ComplaintResponseDto> updateComplaint(@RequestBody ComplaintRequestDto dto, @PathVariable("complaintId") Integer complaintId){
        ComplaintResponseDto updatedComplaint = complaintService.updateComplaint(dto, complaintId);
        return ApiResponse.success(updatedComplaint);
    }

}
