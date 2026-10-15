package com.project.marketplace.controller.admin;

import com.project.marketplace.dto.ComplaintResponseDto;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.ComplaintService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/admin/complaint")
public class AdminComplaintController {

    private final ComplaintService complaintService;

    public AdminComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping("/batch")
    public ApiResponse<List<ComplaintResponseDto>> getAllComplaints(){
        List<ComplaintResponseDto> complaints = complaintService.getAllComplaints();
        return ApiResponse.success(complaints);
    }

    @DeleteMapping("/{complaintId}")
    public ApiResponse<Void> deleteComplaint(@PathVariable("complaintId") Integer complaintId){
        complaintService.deleteComplaint(complaintId);
        return ApiResponse.success();
    }
}
