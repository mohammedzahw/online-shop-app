package com.example.e_commerce.controller.dao;

import java.util.List;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private AddressDao address;
    private List<OrderProductDao> products;

}
