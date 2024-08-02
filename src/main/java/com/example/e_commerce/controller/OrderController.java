package com.example.e_commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.e_commerce.controller.dao.CreateOrderRequest;
import com.example.e_commerce.service.OrderService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "bearerAuth")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 
     * @param orderId
     * @return
     */
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable("orderId") Integer orderId) {
        return orderService.getOrderById(orderId);
    }

    /*****
     * 
     * @param createOrderRequest
     * @return
     */

    @PostMapping("/orders/create")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        // System.out.println("createOrderRequest: " + createOrderRequest);
        return orderService.createOrder(createOrderRequest);
        // return ResponseEntity.ok().build();
    }

    /****
     * 
     * @param orderId
     * @return
     */
    @GetMapping("/orders/delete/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable("orderId") Integer orderId) {
        return orderService.deleteOrder(orderId);
    }

    /****
     * 
     * @return
     */
    @PutMapping("/orders/update/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable("orderId") Integer orderId, @RequestBody CreateOrderRequest createOrderRequest) {
        return orderService.updateOrder(orderId, createOrderRequest);
    }

    /****
     * 
     * @return
     */
    @GetMapping("/orders/cancle/{orderId}")
    public ResponseEntity<?> cancleOrder(@PathVariable("orderId") Integer orderId) {
        return orderService.cancleOrder(orderId);
    }

}
