package com.example.e_commerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.e_commerce.controller.dao.AddProductRequest;
import com.example.e_commerce.controller.dto.ProductDto;
import com.example.e_commerce.exception.CustomException;
import com.example.e_commerce.model.Category;
import com.example.e_commerce.model.Product;
import com.example.e_commerce.repository.CategoryRepository;
import com.example.e_commerce.repository.ProductRepository;

import lombok.Data;

@Service
@Data
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    /****
     * 
     * @param pageNumber
     * @return
     */
    public ResponseEntity<?> getProducts(Integer pageNumber) {
        return ResponseEntity.ok(
                productRepository.findAllProducts(PageRequest.of(pageNumber, 8)).stream()
                        .map(Product -> new ProductDto(Product)).collect(Collectors.toList()));
    }

    /****
     * 
     * @param product
     * @return
     */
    public ResponseEntity<?> addProduct(AddProductRequest product) {
        Category category = categoryRepository.findByName(product.getCategory()).orElseThrow(
                () -> new CustomException("Category not found", HttpStatus.NOT_FOUND));
        Product newProduct = new Product(product, category);
        productRepository.save(newProduct);
        return ResponseEntity.ok("Product added successfully");

    }

    /****
     * 
     * @param productId
     * @return
     */
    public ResponseEntity<?> getProduct(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException("Product not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ProductDto(product));
    }

    /****
     * 
     * @param productId
     * @return
     */
    public ResponseEntity<?> deleteProduct(Integer productId) {
        try {
            productRepository.deleteById(productId);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (Exception e) {
            throw new CustomException("Product not found", HttpStatus.NOT_FOUND);
        }

    }

    /***
     * 
     * @param product
     * @param productId
     * @return
     */

    public ResponseEntity<?> updateProduct(AddProductRequest product, Integer productId) {
        Product oldProduct = productRepository.findById(productId).orElseThrow(
                () -> new CustomException("Product not found", HttpStatus.NOT_FOUND));

        Category category = categoryRepository.findByName(product.getCategory()).orElseThrow(
                () -> new CustomException("Category not found", HttpStatus.NOT_FOUND));
        oldProduct.setName(product.getName());
        oldProduct.setPrice(product.getPrice());
        oldProduct.setCategory(category);
        productRepository.save(oldProduct);

        return ResponseEntity.ok("Product updated successfully");
    }

    /**
     * 
     * @param searchKey
     * @return
     */
    public ResponseEntity<?> getProductsBySearchKey(String searchKey, Integer pageNumber) {

        List<Product> product = productRepository.findBySearchKey(searchKey, PageRequest.of(pageNumber, 8));
        List<ProductDto> productDto = product.stream().map(Product -> new ProductDto(Product))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDto);

    }

    /**
     * 
     * @param categoryId
     * @return
     */

    public ResponseEntity<?> getProductsByCategory(Integer categoryId) {

        List<Product> product = productRepository.findByCategoryId(categoryId);
        List<ProductDto> productDto = product.stream().map(Product -> new ProductDto(Product))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDto);
    }

}
