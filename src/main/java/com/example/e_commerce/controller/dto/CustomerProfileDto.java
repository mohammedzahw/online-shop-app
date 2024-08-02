package com.example.e_commerce.controller.dto;

import com.example.e_commerce.model.Customer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerProfileDto {
    private String name;
    private String email;
    private String phone;
    private Boolean active;
    private String imageUrl;

    public CustomerProfileDto(Customer customer) {
        if (customer == null) {
            return;
        }
        this.name = customer.getName();
        this.email = customer.getEmail();
        this.phone = customer.getPhone();
        this.active = customer.getActive();
        this.imageUrl = customer.getImageUrl();
    }
}
