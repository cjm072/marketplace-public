package com.project.marketplace.repository;

import com.project.marketplace.pojo.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    @Query(value = """
    SELECT p.category AS category, SUM(oi.unit_price * oi.quantity) AS total
    FROM order_item oi
    JOIN product p ON oi.product_id = p.id
    JOIN orders o ON oi.order_id = o.id
    JOIN payment pay ON o.id = pay.order_id
    WHERE YEAR(pay.payment_date) = :year AND MONTH(pay.payment_date) = :month
    GROUP BY p.category
    """, nativeQuery = true)
    List<Object[]> getTotalSalesPerCategoryByMonth(@Param("year") int year, @Param("month") int month);


    @Query(value = """
    SELECT p.name AS productName, SUM(oi.unit_price * oi.quantity) AS total
    FROM order_item oi
    JOIN product p ON oi.product_id = p.id
    JOIN orders o ON oi.order_id = o.id
    JOIN payment pay ON o.id = pay.order_id
    WHERE YEAR(pay.payment_date) = :year
      AND MONTH(pay.payment_date) = :month
      AND p.seller_id = :sellerId
    GROUP BY p.name
""", nativeQuery = true)
    List<Object[]> getTotalSalesPerProductBySellerAndMonth(
            @Param("year") int year,
            @Param("month") int month,
            @Param("sellerId") int sellerId
    );

}
