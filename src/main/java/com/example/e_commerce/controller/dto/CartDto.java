package com.example.e_commerce.controller.dto;

import java.util.List;

import com.example.e_commerce.model.Product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartDto {
    private Double totalPrice = 0.0;
    private List<ProductDto> products;

    public CartDto(List<Product> products) {
        
        products.forEach(product -> this.totalPrice += product.getPrice());
        this.products = products.stream().map(ProductDto::new).toList();

    }

}
