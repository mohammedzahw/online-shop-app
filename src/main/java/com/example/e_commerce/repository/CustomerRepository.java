package com.example.e_commerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.e_commerce.model.Customer;
import com.example.e_commerce.model.Order;
import com.example.e_commerce.model.Product;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByEmail(String email);

    @Query("SELECT c.cart FROM Customer c WHERE c.Id = :CustomerId")
    List<Product> findCart(Integer CustomerId);

    @Query("SELECT c.whishlist FROM Customer c WHERE c.Id = :CustomerId")
    List<Product> findWishlist(Integer CustomerId);

    @Query("SELECT c.orders FROM Customer c WHERE c.Id = :CustomerId")
    List<Order> findOrders(Integer CustomerId);

    @Modifying
    @Query(value = "insert into cart (customer_id, product_id) values (:customerId, :productId)", nativeQuery = true)
    void addToCart(Integer productId, Integer customerId);

    @Modifying
    @Query(value = "delete from cart where product_id = :productId and customer_id = :customerId", nativeQuery = true)
    void deleteFromCart(Integer productId, Integer customerId);

    @Modifying
    @Query(value = "insert into wishlist (customer_id, product_id) values (:customerId, :productId)", nativeQuery = true)
    void addToWishList(Integer productId, Integer customerId);

    @Modifying
    @Query(value = "delete from wishlist where product_id = :productId and customer_id = :customerId", nativeQuery = true)
    void deleteFromWishList(Integer productId, Integer customerId);

}
