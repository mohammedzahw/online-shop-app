package com.example.e_commerce.controller.dto;

import com.example.e_commerce.model.OrderProduct;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderProductDto {
    private Integer id;
    private Integer quantity;
    private ProductDto product;

    public OrderProductDto(OrderProduct orderProduct) {
        if (orderProduct == null) {
            return;
        }
        this.id = orderProduct.getId();
        this.quantity = orderProduct.getQuantity();
        this.product = new ProductDto(orderProduct.getProduct());
    }

}
