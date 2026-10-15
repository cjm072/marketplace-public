package com.project.marketplace.repository;

import com.project.marketplace.pojo.Discount;
import com.project.marketplace.pojo.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    List<Discount> findAllBySeller(Seller seller);
}
