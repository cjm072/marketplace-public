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
public class Payment {

    @Id
    @GeneratedValue
    private Integer id;

    private PaymentMethod method;

    private BigDecimal amount;

    private LocalDate paymentDate;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
