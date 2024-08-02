package com.example.e_commerce.controller.dao;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AddProductRequest {
    @NotEmpty(message = "Name is required")
    private String name;
    @NotEmpty(message = "Short description is required")
    private String shortDeScription;
    @NotEmpty(message = "Integer description is required")
    private String longDeScription;
    @NotEmpty(message = "Price is required")
    private double price;
    private String image;
    @NotEmpty(message = "Quantity is required")
    private Integer quantity;
    @NotEmpty(message = "Category is required")
    private String category;

}
