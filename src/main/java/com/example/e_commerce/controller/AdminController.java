package com.example.e_commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.e_commerce.controller.dao.AddCategoryRequest;
import com.example.e_commerce.controller.dao.AddProductRequest;
import com.example.e_commerce.model.OrderStatus;
import com.example.e_commerce.service.CategoryService;
import com.example.e_commerce.service.OrderService;
import com.example.e_commerce.service.ProductService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OrderService orderService;

    /****
     * 
     * @param product
     * @return
     */
    @PostMapping("/products/product")
    public ResponseEntity<?> addProduct(@RequestBody AddProductRequest product) {
        return productService.addProduct(product);
    }

    @PutMapping("/products/update/{productId}")
    public ResponseEntity<?> updateProduct(@RequestBody AddProductRequest product, @PathVariable Integer productId) {
        return productService.updateProduct(product, productId);
    }

    /****
     * 
     * @param productId
     * @return
     */

    @DeleteMapping("/products/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        return productService.deleteProduct(productId);
    }

    /**
     * 
     * @param category
     * @return
     */
    @PostMapping("/products/category")
    public ResponseEntity<?> addCategory(@RequestBody AddCategoryRequest category) {
        return categoryService.addCategory(category);
    }

    /**
     * 
     * @param category
     * @param categoryId
     * @return
     */
    @PutMapping("/products/category/{categoryId}")
    public ResponseEntity<?> updateCategory(@RequestBody AddCategoryRequest category,
            @PathVariable Integer categoryId) {
        return categoryService.updateCategory(category, categoryId);
    }

    /***
     * 
     * @param categoryId
     * @return
     */
    @DeleteMapping("/products/category/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer categoryId) {
        return categoryService.deleteCategory(categoryId);
    }

    @GetMapping("orders/pending")
    public ResponseEntity<?> getPendingOrders() {
        return orderService.getPendingOrders();
    }

    @GetMapping("orders/shipped")
    public ResponseEntity<?> getShippedOrders() {
        return orderService.getShippedOrders();
    }

    @GetMapping("orders/cancelled")
    public ResponseEntity<?> getCancelledOrders() {
        return orderService.getCancelledOrders();
    }

    @GetMapping("orders/returned")
    public ResponseEntity<?> getReturnedOrders() {
        return orderService.getReturnedOrders();
    }

    @GetMapping("orders/delivered")
    public ResponseEntity<?> getDeliveresOrders() {
        return orderService.getDeliveredOrders();
    }

    @PostMapping("orders/change-status/{orderId}")
    public ResponseEntity<?> changeOrderStatus(@PathVariable Integer orderId,@RequestBody OrderStatus status) {
        return orderService.changeOrderStatus(orderId, status);
    }

}
