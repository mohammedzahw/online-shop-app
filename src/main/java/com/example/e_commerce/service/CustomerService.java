package com.example.e_commerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.e_commerce.controller.dto.CartDto;
import com.example.e_commerce.controller.dto.CustomerProfileDto;
import com.example.e_commerce.controller.dto.OrderDto;
import com.example.e_commerce.exception.CustomException;
import com.example.e_commerce.model.Customer;
import com.example.e_commerce.repository.CustomerRepository;
import com.example.e_commerce.repository.OrderRepository;
import com.example.e_commerce.security.TokenUtil;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private OrderRepository orderRepository;

    /****
     * 
     * @param Id
     * @return
     */
    public ResponseEntity<?> getCustomer() {
        Customer customer = customerRepository.findById(tokenUtil.getUserId()).orElseThrow(
                () -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new CustomerProfileDto(customer));

    }

    /****
     * 
     * @param customerId
     * @return
     */
    public ResponseEntity<?> deleteCustomer() {

        Customer customer = customerRepository.findById(tokenUtil.getUserId()).orElseThrow(
                () -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));
        customerRepository.delete(customer);
        return ResponseEntity.ok("Customer deleted successfully");
    }

    /****
     * 
     * @param customer
     * @return
     */
    public ResponseEntity<?> updateCustomer(CustomerProfileDto customerDto) {
        Customer customer = customerRepository.findById(tokenUtil.getUserId()).orElseThrow(
                () -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));
        customer.setName(customer.getName());
        customer.setEmail(customer.getEmail());
        customer.setPhone(customer.getPhone());
        customerRepository.save(customer);
        return ResponseEntity.ok("Customer updated successfully");
    }

    /***
     * 
     * @return
     */
    public ResponseEntity<?> getCart() {
        // Customer customer =
        // customerRepository.findById(tokenUtil.getUserId()).orElseThrow(
        // () -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new CartDto(
                customerRepository.findCart(tokenUtil.getUserId())));
    }

    /**
     * 
     * @return
     */
    public ResponseEntity<?> getWishList() {
        return ResponseEntity.ok(new CartDto(
                customerRepository.findWishlist(tokenUtil.getUserId())));
    }

    /**
     * 
     * @param productId
     * @return
     */
    public ResponseEntity<?> addToCart(Integer productId) {
        Customer customer = customerRepository.findById(tokenUtil.getUserId()).orElseThrow(
                () -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));
        customerRepository.addToCart(productId, customer.getId());
        return ResponseEntity.ok("Product added to cart successfully");

    }

    /**
     * 
     * @param productId
     * @return
     */
    public ResponseEntity<?> deleteFromCart(Integer productId) {
        Customer customer = customerRepository.findById(tokenUtil.getUserId()).orElseThrow(
                () -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));
        customerRepository.deleteFromCart(productId, customer.getId());
        return ResponseEntity.ok("Product deleted from cart successfully");
    }

    /**
     * 
     * @param productId
     * @return
     */

    public ResponseEntity<?> addToWishList(Integer productId) {
        Customer customer = customerRepository.findById(tokenUtil.getUserId()).orElseThrow(
                () -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));
        customerRepository.addToWishList(productId, customer.getId());
        return ResponseEntity.ok("Product added to wishlist successfully");
    }

    /**
     * 
     * @param productId
     * @return
     */
    public ResponseEntity<?> deleteFromWishList(Integer productId) {
        Customer customer = customerRepository.findById(tokenUtil.getUserId()).orElseThrow(
                () -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));
        customerRepository.deleteFromWishList(productId, customer.getId());
        return ResponseEntity.ok("Product deleted from wishlist successfully");
    }

    /**
     * 
     * @return
     */

    public ResponseEntity<?> getOrders() {
        Customer customer = customerRepository.findById(tokenUtil.getUserId()).orElseThrow(
                () -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));
        List<OrderDto> orders = customerRepository.findOrders(customer.getId()).stream().map(
                order -> new OrderDto(order, orderRepository.findOrderProducts(order.getId()))

        ).collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

}
