package com.project.marketplace.repository;

import com.project.marketplace.pojo.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
}
