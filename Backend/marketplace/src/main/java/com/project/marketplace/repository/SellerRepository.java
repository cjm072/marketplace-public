package com.project.marketplace.repository;

import com.project.marketplace.pojo.AccountStatus;
import com.project.marketplace.pojo.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerRepository extends JpaRepository<Seller, Integer> {
    List<Seller> findByAccountStatus(AccountStatus accountStatus);
}
