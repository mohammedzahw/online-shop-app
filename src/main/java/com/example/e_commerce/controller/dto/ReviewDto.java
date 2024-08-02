package com.example.e_commerce.controller.dto;

import com.example.e_commerce.model.Customer;
import com.example.e_commerce.model.Review;

import lombok.Data;

@Data
public class ReviewDto {
    private Integer id;
    private String review;
    private Integer rating;
    private Boolean isTheOwner;
    private Boolean markedAsHelpFull;
    private CustomerDto customer;
    private Integer helpFullCount;

    public ReviewDto(Review review, Customer customer, Boolean isTheOwner, Boolean markedAsHelpFull) {
        if (review == null) {
            return;
        }
        this.id = review.getId();
        this.review = review.getReview();
        this.rating = review.getRating();
        this.customer = new CustomerDto(review.getCustomer());
        this.helpFullCount = review.getHelpFullCount();
        this.customer = new CustomerDto(customer);
        this.isTheOwner = isTheOwner;
        this.markedAsHelpFull = markedAsHelpFull;
    }

}
