package com.example.e_commerce.controller.dto;

import com.example.e_commerce.model.Customer;

import lombok.Data;

@Data
public class CustomerDto {
    private Integer id;
    private String name;
    private Boolean active;
    private String imageUrl;

    public CustomerDto(Customer customer) {
        if (customer == null) {
            return;
        }
        this.id = customer.getId();
        this.name = customer.getName();
        this.active = customer.getActive();
        this.imageUrl = customer.getImageUrl();
    }

}
