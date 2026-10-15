package com.project.marketplace.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String description;

    private BigDecimal price;

    private String image;

    private int quantity;

    private ProductCategory category;

    private ProductStatus status;

    private BigDecimal weight;

    private String materials;

    private String certifications;

    private int co2;

    @OneToMany(mappedBy = "product")
    private List<Review> reviews;

    @ManyToMany
    @JoinTable(
            name = "products_discounts",
            joinColumns = {
                    @JoinColumn(name = "product_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "discount_id")
            }
    )
    private List<Discount> discounts;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;
}
