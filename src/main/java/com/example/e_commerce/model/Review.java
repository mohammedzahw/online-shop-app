package com.example.e_commerce.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringExclude;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String review;
    private Integer rating;
    private Integer helpFullCount = 0;
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @ToStringExclude
    private Product product;
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @ToStringExclude
    private Customer customer;

    @ManyToMany(fetch = jakarta.persistence.FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @JoinTable(name = "helpfull", joinColumns = @JoinColumn(name = "review_id"), inverseJoinColumns = @JoinColumn(name = "customer_id"))
    private List<Customer> helpFull;

    public Review(String review, Integer rating, Product product, Customer customer) {
        this.review = review;
        this.rating = rating;
        this.product = product;
        this.customer = customer;
    }
}
