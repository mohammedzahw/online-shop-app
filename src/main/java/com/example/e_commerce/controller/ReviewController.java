package com.example.e_commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.e_commerce.controller.dao.CreateReviewRequest;
import com.example.e_commerce.service.ReviewService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
@NoArgsConstructor
@AllArgsConstructor
@Data
@SecurityRequirement(name = "bearerAuth")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    /****
     * 
     * @param productId
     * @return
     */
    @GetMapping("/reviews/{productId}")
    public ResponseEntity<?> getReviews(@PathVariable("productId") Integer productId) {
        return reviewService.getReviews(productId);
    }

    /***
     * 
     * @param productId
     * @param review
     * @return
     */
    @PostMapping("/reviews/create/{productId}")
    public ResponseEntity<?> addReview(@PathVariable("productId") Integer productId,
            @RequestBody CreateReviewRequest review) {
        return reviewService.addReview(productId, review);
    }

    /****
     * 
     * @param productId
     * @return
     */
    @GetMapping("/reviews/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable("reviewId") Integer reviewId) {
        return reviewService.deleteReview(reviewId);
    }

    /****
     * 
     * @param productId
     * @return
     */
    @PostMapping("/reviews/update/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable("reviewId") Integer reviewId,
            @RequestBody CreateReviewRequest review) {
        return reviewService.updateReview(reviewId, review);
    }

    /****
     * 
     * @param reviewId
     * @return
     */
    @GetMapping("/reviews/{reviewId}/helpful")
    public ResponseEntity<?> helpfulReview(@PathVariable("reviewId") Integer reviewId) {
        return reviewService.helpfulReview(reviewId);

    }

    @GetMapping("/reviews/{reviewId}/unhelpful")
    public ResponseEntity<?> unhelpfulReview(@PathVariable("reviewId") Integer reviewId) {
        return reviewService.unhelpfulReview(reviewId);
    }
}
