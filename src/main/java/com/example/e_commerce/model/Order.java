package com.example.e_commerce.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    // @ToString.Exclude
    private Address address;
    @OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "order")
    private List<OrderProduct> products;

    private LocalDateTime date;
    private double totalPrice;

    public void setOrder(Customer customer, Address address, List<OrderProduct> products) {
        this.customer = customer;
        this.address = address;
        this.products = products;
        this.date = LocalDateTime.now();
        this.totalPrice = products.stream().mapToDouble(OrderProduct::getTotalPrice).sum();
        this.status = OrderStatus.PENDING;
    }

}
