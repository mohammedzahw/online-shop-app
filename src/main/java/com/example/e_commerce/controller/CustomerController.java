package com.example.e_commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.e_commerce.controller.dto.CustomerProfileDto;
import com.example.e_commerce.service.CustomerService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    /****
     * 
     * @param
     * @return
     */
    @GetMapping("/customers/customer")
    public ResponseEntity<?> getCustomer() {
        return customerService.getCustomer();
    }

    /****
     * 
     * @param
     * @return
     */
    @GetMapping("/customers/delete")
    public ResponseEntity<?> deleteCustomer() {
        return customerService.deleteCustomer();
    }

    /****
     * 
     * @param CustomerDto
     * @return
     */

    @PutMapping("/customers/update")
    public ResponseEntity<?> updateCustomer(CustomerProfileDto customerDto) {
        return customerService.updateCustomer(customerDto);
    }

    /****
     * 
     * @param
     * @return
     */
    @GetMapping("/customers/cart")
    public ResponseEntity<?> getCart() {
        return customerService.getCart();
    }

    /****
     * 
     * @param productId
     * @return
     */
    @PostMapping("/customers/cart/add/{productId}")
    public ResponseEntity<?> addToCart(@PathVariable("productId") Integer productId) {

        return customerService.addToCart(productId);
    }

    /****
     * 
     * @param productId
     * @return
     */

    @DeleteMapping("/customers/cart/delete/{productId}")
    public ResponseEntity<?> deleteFromCart(@PathVariable("productId") Integer productId) {
        return customerService.deleteFromCart(productId);
    }

    /****
     * 
     * @return
     */
    @GetMapping("/customers/wishlist")
    public ResponseEntity<?> getWishList() {
        return customerService.getWishList();
    }

    /****
     * 
     * @param productId
     * @return
     */
    @PostMapping("/customers/wishlist/add/{productId}")
    public ResponseEntity<?> addToWishList(@PathVariable("productId") Integer productId) {
        return customerService.addToWishList(productId);
    }

    /****
     * 
     * @param productId
     * @return
     */
    @DeleteMapping("/customers/wishlist/delete/{productId}")
    public ResponseEntity<?> deleteFromWishList(@PathVariable("productId") Integer productId) {
        return customerService.deleteFromWishList(productId);
    }

    /****
     * 
     * @return
     */
    @GetMapping("/customers/orders")
    public ResponseEntity<?> getOrders() {
        return customerService.getOrders();
    }

}
