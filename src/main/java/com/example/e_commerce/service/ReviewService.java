package com.example.e_commerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.e_commerce.controller.dao.CreateReviewRequest;
import com.example.e_commerce.controller.dto.ReviewDto;
import com.example.e_commerce.exception.CustomException;
import com.example.e_commerce.model.Customer;
import com.example.e_commerce.model.Product;
import com.example.e_commerce.model.Review;
import com.example.e_commerce.repository.CustomerRepository;
import com.example.e_commerce.repository.ProductRepository;
import com.example.e_commerce.repository.ReviewRepository;
import com.example.e_commerce.security.TokenUtil;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Data
@Transactional
public class ReviewService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    /**
     * 
     * @param productId
     * @return
     */

    public ResponseEntity<?> getReviews(Integer productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new CustomException("Product not found", HttpStatus.NOT_FOUND));
        List<Review> reviews = reviewRepository.findByProduct(product);

        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (Review review : reviews) {
            Customer customer = reviewRepository.findCustomerByReviewId(review.getId()).orElse(null);
            if (customer == null) {
                continue;
            }
            // System.out.println(customer);

            reviewDtos.addFirst(
                    new ReviewDto(
                            review, customer,
                            customer.getId().equals(tokenUtil.getUserId()),
                            reviewRepository.checkhelpfulExist(review.getId(), customer.getId()).isPresent()));

        }

        return ResponseEntity.ok(reviewDtos);
    }

    /**
     * 
     * @param productId
     * @param review
     * @return
     */

    public ResponseEntity<?> addReview(Integer productId, CreateReviewRequest review) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new CustomException("Product not found", HttpStatus.NOT_FOUND));
        Customer customer = customerRepository.findById(tokenUtil.getUserId()).orElseThrow(
                () -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));
        if (reviewRepository.findByProductAndCustomer(product, customer).isPresent()) {
            return ResponseEntity.badRequest().body("You have already reviewed this product");
        }
        Review newReview = new Review(review.getReview(), review.getRating(), product, customer);
        productRepository.save(product);
        reviewRepository.save(newReview);
        return ResponseEntity.ok("Review added successfully");

    }

    /**
     * 
     * @param productId
     * @return
     */

    public ResponseEntity<?> deleteReview(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new CustomException("Review not found", HttpStatus.NOT_FOUND));
        Customer customer = reviewRepository.findCustomerByReviewId(review.getId()).orElseThrow(
                () -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));

        if (!customer.getId().equals(tokenUtil.getUserId())) {
            return ResponseEntity.badRequest().body("You are not authorized to delete this review");
        }
        reviewRepository.delete(review);
        return ResponseEntity.ok(
                "Review deleted successfully");
    }

    /**
     * 
     * @param productId
     * @return
     */

    public ResponseEntity<?> updateReview(Integer reviewId, CreateReviewRequest review) {
        Review reviewToUpdate = reviewRepository.findById(reviewId).orElseThrow(
                () -> new CustomException("Review not found", HttpStatus.NOT_FOUND));
        Customer customer = reviewRepository.findCustomerByReviewId(reviewToUpdate.getId()).orElseThrow(
                () -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));

        if (!customer.getId().equals(tokenUtil.getUserId())) {
            return ResponseEntity.badRequest().body("You are not authorized to update this review");
        }
        reviewToUpdate.setReview(review.getReview());
        reviewToUpdate.setRating(review.getRating());
        reviewRepository.save(reviewToUpdate);
        return ResponseEntity.ok("Review updated successfully");
    }

    /**
     * 
     * @param reviewId
     * @return
     */
    public ResponseEntity<?> helpfulReview(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new CustomException("Review not found", HttpStatus.NOT_FOUND));
        Customer customer = customerRepository.findById(tokenUtil.getUserId()).orElseThrow(
                () -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));
        if (reviewRepository.checkhelpfulExist(review.getId(), customer.getId()).isPresent()) {
            return ResponseEntity.badRequest().body("You have already marked this review as helpful");
        }
        ;
        reviewRepository.addHelpFull(review.getId(), customer.getId());
        review.setHelpFullCount(review.getHelpFullCount() + 1);
        reviewRepository.save(review);
        return ResponseEntity.ok("Review marked as helpful");
    }

    /**
     * 
     * @param reviewId
     * @return
     */

    public ResponseEntity<?> unhelpfulReview(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new CustomException("Review not found", HttpStatus.NOT_FOUND));
        Customer customer = customerRepository.findById(tokenUtil.getUserId()).orElseThrow(
                () -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));
        reviewRepository.removeHelpFull(review.getId(), customer.getId());
        review.setHelpFullCount(review.getHelpFullCount() - 1);
        reviewRepository.save(review);
        return ResponseEntity.ok("Review marked as unhelpful");

    }

}
