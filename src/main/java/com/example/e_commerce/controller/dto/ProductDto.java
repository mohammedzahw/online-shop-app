package com.example.e_commerce.controller.dto;

import com.example.e_commerce.model.Category;
import com.example.e_commerce.model.Product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDto {
    Integer id;
    private String name;

    private String shortDeScription;

    private String longDeScription;

    private double price;
    private String image;

    private Integer quantity;

    private Category category;

    public ProductDto(Product product) {
        if (product == null) {
            return;
        }
        this.id = product.getId();
        this.name = product.getName();
        this.shortDeScription = product.getShortDescription();
        this.longDeScription = product.getLongDescription();
        this.price = product.getPrice();
        this.image = product.getImage();
        this.quantity = product.getQuantity();
        this.category = product.getCategory();
    }
}
