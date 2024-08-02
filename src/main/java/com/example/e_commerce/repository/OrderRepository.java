package com.example.e_commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.e_commerce.model.Order;
import com.example.e_commerce.model.OrderProduct;
import com.example.e_commerce.model.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o.products FROM Order o WHERE o.Id = :orderID")
    List<OrderProduct> findOrderProducts(Integer orderID);

    List<Order> findByStatus(OrderStatus pending);

}