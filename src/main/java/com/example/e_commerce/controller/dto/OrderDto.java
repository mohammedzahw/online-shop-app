package com.example.e_commerce.controller.dto;

import java.util.List;

import com.example.e_commerce.model.Address;
import com.example.e_commerce.model.Order;
import com.example.e_commerce.model.OrderProduct;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDto {
    private Integer id;

    private Address address;

    private List<OrderProductDto> products;

    private double totalPrice;

    public OrderDto(Order order, List<OrderProduct> products) {
        if (order == null) {
            return;
        }
        this.id = order.getId();
        this.address = order.getAddress();
        this.totalPrice = order.getTotalPrice();
        this.products = products.stream().map(OrderProductDto::new).toList();
    }
}
