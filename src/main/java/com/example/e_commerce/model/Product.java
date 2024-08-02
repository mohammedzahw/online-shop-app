package com.example.e_commerce.model;

import java.util.ArrayList;
import java.util.List;

import com.example.e_commerce.controller.dao.AddProductRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String shortDescription;
    private String longDescription;
    private double price;
    private String image;
    private int quantity;
    // @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    // private List<OrderProduct> orders;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Review> reviews;

    public Product(AddProductRequest product, Category category) {
        if (product == null) {
            return;
        }
        this.name = product.getName();
        this.shortDescription = product.getShortDeScription();
        this.longDescription = product.getLongDeScription();
        this.price = product.getPrice();
        this.image = product.getImage();
        this.quantity = product.getQuantity();
        this.category = category;
        this.reviews = new ArrayList<>();

    }

}
