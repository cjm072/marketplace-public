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
public class Customer extends User {

    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Cart cart;

    @ManyToMany
    @JoinTable(
            name = "customers_vouchers",
            joinColumns = {
                    @JoinColumn(name = "customer_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "voucher_id")
            }
    )
    private List<Voucher> vouchers;

    @OneToMany(mappedBy = "customer")
    private List<Review> reviews;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

    @OneToMany(mappedBy = "customer")
    private List<Complaint> complaints;
}
