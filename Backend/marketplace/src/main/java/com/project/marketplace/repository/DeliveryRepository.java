package com.project.marketplace.repository;

import com.project.marketplace.pojo.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
}
