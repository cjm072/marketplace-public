package com.project.marketplace.repository;

import com.project.marketplace.pojo.Product;
import com.project.marketplace.pojo.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findAllByProduct(Product product);
}
