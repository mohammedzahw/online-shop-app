package com.example.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.e_commerce.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {

}
