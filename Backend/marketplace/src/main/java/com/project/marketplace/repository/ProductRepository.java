package com.project.marketplace.repository;

import com.project.marketplace.pojo.Product;
import com.project.marketplace.pojo.ProductStatus;
import com.project.marketplace.pojo.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllBySeller(Seller seller);
    List<Product> findByStatus(ProductStatus status);
    List<Product> findAllByNameContainingIgnoreCase(String name);
}
