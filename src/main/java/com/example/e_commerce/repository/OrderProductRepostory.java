package com.example.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.e_commerce.model.OrderProduct;

public interface OrderProductRepostory extends JpaRepository<OrderProduct, Integer> {

}
