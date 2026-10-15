package com.project.marketplace.repository;

import com.project.marketplace.pojo.Complaint;
import com.project.marketplace.pojo.Customer;
import com.project.marketplace.pojo.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {
    List<Complaint> findAllByCustomer(Customer customer);
    List<Complaint> findAllBySeller(Seller seller);
}
