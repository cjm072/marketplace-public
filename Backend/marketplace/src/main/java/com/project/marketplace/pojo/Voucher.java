package com.project.marketplace.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Voucher {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate expiryDate;

    private BigDecimal amount;

    private ProductCategory category;

    private int quantity;

    @ManyToMany(mappedBy = "vouchers")
    private List<Customer> customers;

    @OneToMany(mappedBy = "voucher")
    private List<Order> orders;
}
