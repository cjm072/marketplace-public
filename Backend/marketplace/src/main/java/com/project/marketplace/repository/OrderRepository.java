package com.project.marketplace.repository;

import com.project.marketplace.pojo.Customer;
import com.project.marketplace.pojo.Order;
import com.project.marketplace.pojo.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByCustomer(Customer customer);
    List<Order> findAllBySeller(Seller seller);
}
