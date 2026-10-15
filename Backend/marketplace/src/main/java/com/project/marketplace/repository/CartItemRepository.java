package com.project.marketplace.repository;

import com.project.marketplace.pojo.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
}
