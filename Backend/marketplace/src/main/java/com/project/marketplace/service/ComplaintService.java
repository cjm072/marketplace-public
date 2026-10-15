package com.project.marketplace.service;

import com.project.marketplace.dto.ComplaintRequestDto;
import com.project.marketplace.dto.ComplaintResponseDto;

import java.util.List;

public interface ComplaintService {
    List<ComplaintResponseDto> getAllComplaints();
    List<ComplaintResponseDto> getAllComplaintsByCustomer(Integer customerId);
    List<ComplaintResponseDto> getAllComplaintsBySeller(Integer sellerId);
    ComplaintResponseDto addComplaint(ComplaintRequestDto dto);
    ComplaintResponseDto updateComplaint(ComplaintRequestDto dto, Integer complaintId);
    void deleteComplaint(Integer complaintId);
}
