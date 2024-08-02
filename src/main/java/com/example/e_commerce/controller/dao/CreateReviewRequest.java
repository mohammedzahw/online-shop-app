package com.example.e_commerce.controller.dao;

import lombok.Data;

@Data
public class CreateReviewRequest {
    private String review;
    private Integer rating;
}
