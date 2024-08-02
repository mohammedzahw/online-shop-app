package com.example.e_commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.e_commerce.controller.dao.AddCategoryRequest;
import com.example.e_commerce.exception.CustomException;
import com.example.e_commerce.model.Category;
import com.example.e_commerce.repository.CategoryRepository;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * 
     * @param pageNumber
     * @return
     */
    public ResponseEntity<?> getCategories() {

        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    /**
     * 
     * @param categoryId
     * @return
     */

    public ResponseEntity<?> addCategory(AddCategoryRequest addCategoryRequest) {
        Category category = new Category();
        category.setName(addCategoryRequest.getName());
        category.setDescription(addCategoryRequest.getDescription());
        categoryRepository.save(category);

        return ResponseEntity.ok("Category added successfully");
    }

    /**
     * 
     * @param categoryId
     * @return
     */
    public ResponseEntity<?> deleteCategory(Integer categoryId) {
        categoryRepository.deleteById(categoryId);
        return ResponseEntity.ok("Category deleted successfully");
    }
    /***
     * 
     * @param category
     * @param categoryId
     * @return
     */

    public ResponseEntity<?> updateCategory(AddCategoryRequest category, Integer categoryId) {

        Category oldCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new CustomException("Category not found", HttpStatus.NOT_FOUND));
        oldCategory.setName(category.getName());
        oldCategory.setDescription(category.getDescription());
        categoryRepository.save(oldCategory);
        return ResponseEntity.ok("Category updated successfully");
    }
}
