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
public class Discount {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String description;

    private BigDecimal amount;

    private  LocalDate startDate;

    private LocalDate expiryDate;

    @ManyToMany(mappedBy = "discounts")
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;
}
