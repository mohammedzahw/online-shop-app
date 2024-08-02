package com.example.e_commerce.controller.dao;

import lombok.Data;

@Data
public class AddressDao {
    
    private String country;
    private String city;
    private String state;
    private String street;
    private Integer homeNumber;
    private Integer zip;
    private String phone;
}
