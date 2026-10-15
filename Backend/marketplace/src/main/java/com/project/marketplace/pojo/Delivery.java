package com.project.marketplace.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Delivery {

    @Id
    @GeneratedValue
    private Integer id;

    private DeliveryMethod method;

    private BigDecimal price;

    private BigDecimal emissionRate;

    private String courier;

    private LocalDate dispatchedAt;

    private LocalDate deliveredAt;

    @Embedded
    private Address shippingAddress;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
