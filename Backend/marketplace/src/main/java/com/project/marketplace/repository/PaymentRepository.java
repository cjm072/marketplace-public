package com.project.marketplace.repository;

import com.project.marketplace.pojo.Customer;
import com.project.marketplace.pojo.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    @Query("SELECT p FROM Payment p WHERE p.order.customer = :customer")
    List<Payment> findAllByCustomer(Customer customer);
    @Query("SELECT MONTH(p.paymentDate), COUNT(p) " +
            "FROM Payment p " +
            "WHERE YEAR(p.paymentDate) = :year " +
            "GROUP BY MONTH(p.paymentDate) " +
            "ORDER BY MONTH(p.paymentDate)")
    List<Object[]> countPaymentsPerMonth(int year);
    @Query("SELECT MONTH(p.paymentDate), SUM(p.amount) " +
            "FROM Payment p " +
            "WHERE YEAR(p.paymentDate) = :year " +
            "GROUP BY MONTH(p.paymentDate) " +
            "ORDER BY MONTH(p.paymentDate)")
    List<Object[]> sumPaymentAmountPerMonth(int year);
    @Query("SELECT MONTH(p.paymentDate), AVG(p.amount) " +
            "FROM Payment p " +
            "WHERE YEAR(p.paymentDate) = :year " +
            "GROUP BY MONTH(p.paymentDate) " +
            "ORDER BY MONTH(p.paymentDate)")
    List<Object[]> averageAmountPerMonth(int year);

    @Query("SELECT MONTH(p.paymentDate), COUNT(p) " +
            "FROM Payment p " +
            "WHERE YEAR(p.paymentDate) = :year AND p.order.seller.id = :sellerId " +
            "GROUP BY MONTH(p.paymentDate) " +
            "ORDER BY MONTH(p.paymentDate)")
    List<Object[]> countPaymentsPerMonthBySeller(int year, Integer sellerId);

    @Query("SELECT MONTH(p.paymentDate), SUM(p.amount) " +
            "FROM Payment p " +
            "WHERE YEAR(p.paymentDate) = :year AND p.order.seller.id = :sellerId " +
            "GROUP BY MONTH(p.paymentDate) " +
            "ORDER BY MONTH(p.paymentDate)")
    List<Object[]> sumPaymentAmountPerMonthBySeller(int year, Integer sellerId);

    @Query("SELECT MONTH(p.paymentDate), AVG(p.amount) " +
            "FROM Payment p " +
            "WHERE YEAR(p.paymentDate) = :year AND p.order.seller.id = :sellerId " +
            "GROUP BY MONTH(p.paymentDate) " +
            "ORDER BY MONTH(p.paymentDate)")
    List<Object[]> averageAmountPerMonthBySeller(int year, Integer sellerId);

}
