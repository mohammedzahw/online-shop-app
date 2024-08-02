package com.example.e_commerce.model;

import com.example.e_commerce.controller.dao.AddressDao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String country;
    private String city;
    private String state;
    private String street;
    private Integer homeNumber;
    private Integer zip;
    private String phone;

    public Address(AddressDao address) {
        if (address == null) {
            return;
        }
        this.country = address.getCountry();
        this.city = address.getCity();
        this.state = address.getState();
        this.street = address.getStreet();
        this.homeNumber = address.getHomeNumber();
        this.zip = address.getZip();
        this.phone = address.getPhone();
    }
    // @OneToOne
    // private Customer customer;

}
