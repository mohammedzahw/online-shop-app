package com.example.e_commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.e_commerce.service.CategoryService;
import com.example.e_commerce.service.ProductService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    /*****
     * 
     * @param pageNumber
     * @return
     */
    @GetMapping("/products/{pageNumber}")
    public ResponseEntity<?> getProducts(@PathVariable Integer pageNumber) {
        return productService.getProducts(pageNumber);
    }

    /****
     * 
     * @param productId
     * @return
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Integer productId) {
        return productService.getProduct(productId);
    }

    /**
     * 
     * @param categoryId
     * @return
     */

    @GetMapping("products/category/{categoryId}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable Integer categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    /**
     * 
     * @param searchKey
     * @return
     */

    @GetMapping("products/search/{searchKey}/{pageNumber}")
    public ResponseEntity<?> getProductsBySearchKey(@PathVariable String searchKey, @PathVariable Integer pageNumber) {
        return productService.getProductsBySearchKey(searchKey, pageNumber);
    }

    /**
     * 
     * @return
     */
    @GetMapping("products/categories")
    public ResponseEntity<?> getCategories() {
        return categoryService.getCategories();
    }

}
