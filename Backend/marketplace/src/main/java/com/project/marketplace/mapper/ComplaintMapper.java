package com.project.marketplace.mapper;

import com.project.marketplace.dto.ComplaintRequestDto;
import com.project.marketplace.dto.ComplaintResponseDto;
import com.project.marketplace.pojo.Complaint;
import com.project.marketplace.pojo.Customer;
import com.project.marketplace.pojo.OrderItem;
import com.project.marketplace.pojo.Seller;
import com.project.marketplace.repository.CustomerRepository;
import com.project.marketplace.repository.OrderItemRepository;
import com.project.marketplace.repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ComplaintMapper {

    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final OrderItemRepository orderItemRepository;

    public ComplaintMapper(CustomerRepository customerRepository, SellerRepository sellerRepository, OrderItemRepository orderItemRepository) {
        this.customerRepository = customerRepository;
        this.sellerRepository = sellerRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Complaint toComplaint(ComplaintRequestDto dto) {
        if (dto == null) {
            return null;
        }

        OrderItem orderItem = orderItemRepository.findById(dto.orderItemId())
                .orElseThrow(() -> new NoSuchElementException("Order item with ID " + dto.orderItemId() + " not found"));

        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new NoSuchElementException("Customer with ID " + dto.customerId() + " not found"));

        Seller seller = sellerRepository.findById(dto.sellerId())
                .orElseThrow(() -> new NoSuchElementException("Seller with ID " + dto.sellerId() + " not found"));

        Complaint complaint = new Complaint();
        complaint.setIssue(dto.issue());
        complaint.setOrderItem(orderItem);
        complaint.setCustomer(customer);
        complaint.setSeller(seller);

        return complaint;
    }

    public ComplaintResponseDto toComplaintResponseDto(Complaint complaint){
        if ( complaint == null ) {
            return null;
        }

        return new ComplaintResponseDto(
                complaint.getId(),
                complaint.getIssue(),
                complaint.getOrderItem().getId(),
                complaint.getCustomer().getId(),
                complaint.getSeller().getId()
        );
    }

    public List<ComplaintResponseDto> toComplaintResponseDtoList(List<Complaint> complaints){
        if ( complaints == null ) {
            return null;
        }
        List<ComplaintResponseDto> list = new ArrayList<>( complaints.size() );
        for ( Complaint complaint : complaints ) {
            list.add( toComplaintResponseDto( complaint ) );
        }

        return list;
    }
}