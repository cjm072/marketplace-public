package com.project.marketplace.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Seller extends User {

    @Id
    @GeneratedValue
    private Integer id;

    private AccountStatus accountStatus;

    private String company;

    @OneToMany(mappedBy = "seller")
    private List<Complaint> complaints;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Product> products;

    @OneToMany(mappedBy = "seller")
    private List<Order> orders;

    @OneToMany(mappedBy = "seller")
    private List<Discount> discounts;
}
