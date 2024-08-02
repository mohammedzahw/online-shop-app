package com.example.e_commerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.e_commerce.model.Customer;
import com.example.e_commerce.model.Product;
import com.example.e_commerce.model.Review;

import jakarta.transaction.Transactional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Optional<Review> findByProductAndCustomer(Product product, Customer customer);

    List<Review> findByProduct(Product product);

    @Transactional
    @Query("SELECT r.customer FROM Review r WHERE r.Id = :reviewId")
    Optional<Customer> findCustomerByReviewId(Integer reviewId);

    void deleteByProductAndCustomer(Product product, Customer customer);

    @Modifying
    @Query(value = "insert into helpfull (review_id, customer_id) values (:reviewId, :customerId)", nativeQuery = true)
    void addHelpFull(Integer reviewId, Integer customerId);

    @Modifying
    @Query(value = "delete from helpfull where review_id = :reviewId and customer_id = :customer_id", nativeQuery = true)
    void removeHelpFull(Integer reviewId, Integer customer_id);

    @Query(value = "select review_id from helpfull where review_id = :reviewId and customer_id = :customer_id", nativeQuery = true)
    Optional<Integer> checkhelpfulExist(Integer reviewId, Integer customer_id);

}
