package com.project.marketplace.service;

import com.project.marketplace.dto.ComplaintRequestDto;
import com.project.marketplace.dto.ComplaintResponseDto;
import com.project.marketplace.mapper.ComplaintMapper;
import com.project.marketplace.pojo.Complaint;
import com.project.marketplace.pojo.Customer;
import com.project.marketplace.pojo.Seller;
import com.project.marketplace.repository.ComplaintRepository;
import com.project.marketplace.repository.CustomerRepository;
import com.project.marketplace.repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ComplaintServiceImpl implements ComplaintService{
    private final ComplaintRepository complaintRepository;
    private final ComplaintMapper complaintMapper;
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;

    public ComplaintServiceImpl(ComplaintRepository complaintRepository, ComplaintMapper complaintMapper, CustomerRepository customerRepository, SellerRepository sellerRepository) {
        this.complaintRepository = complaintRepository;
        this.complaintMapper = complaintMapper;
        this.customerRepository = customerRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public List<ComplaintResponseDto> getAllComplaints(){
        List<Complaint> complaints = complaintRepository.findAll();
        return complaintMapper.toComplaintResponseDtoList(complaints);
    }

    @Override
    public List<ComplaintResponseDto> getAllComplaintsByCustomer(Integer customerId){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer with ID " + customerId + " not found"));
        List<Complaint> complaints = complaintRepository.findAllByCustomer(customer);
        return complaintMapper.toComplaintResponseDtoList(complaints);
    }

    @Override
    public List<ComplaintResponseDto> getAllComplaintsBySeller(Integer sellerId){
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new NoSuchElementException("Seller with ID " + sellerId + " not found"));
        List<Complaint> complaints = complaintRepository.findAllBySeller(seller);
        return complaintMapper.toComplaintResponseDtoList(complaints);
    }

    @Override
    public ComplaintResponseDto addComplaint(ComplaintRequestDto dto){
        Complaint complaint = complaintMapper.toComplaint(dto);
        Complaint savedComplaint = complaintRepository.save(complaint);
        return complaintMapper.toComplaintResponseDto(savedComplaint);
    }

    @Override
    public ComplaintResponseDto updateComplaint(ComplaintRequestDto dto, Integer complaintId){
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new NoSuchElementException("Complaint with ID " + complaintId + " not found"));
        complaint.setIssue(dto.issue());
        Complaint savedcomplaint = complaintRepository.save(complaint);
        return complaintMapper.toComplaintResponseDto(savedcomplaint);
    }

    @Override
    public void deleteComplaint(Integer complaintId){
        complaintRepository.deleteById(complaintId);
    }
}
