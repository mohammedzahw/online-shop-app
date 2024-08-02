package com.example.e_commerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.e_commerce.controller.dao.CreateOrderRequest;
import com.example.e_commerce.controller.dao.OrderProductDao;
import com.example.e_commerce.controller.dto.OrderDto;
import com.example.e_commerce.exception.CustomException;
import com.example.e_commerce.model.Address;
import com.example.e_commerce.model.Customer;
import com.example.e_commerce.model.Order;
import com.example.e_commerce.model.OrderProduct;
import com.example.e_commerce.model.OrderStatus;
import com.example.e_commerce.model.Product;
import com.example.e_commerce.repository.AddressRepository;
import com.example.e_commerce.repository.CustomerRepository;
import com.example.e_commerce.repository.OrderProductRepostory;
import com.example.e_commerce.repository.OrderRepository;
import com.example.e_commerce.repository.ProductRepository;
import com.example.e_commerce.security.TokenUtil;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderProductRepostory orderProductRepostory;

    /**
     * 
     * @param orderId
     * @return
     */
    public ResponseEntity<?> getOrderById(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new OrderDto(order, orderRepository.findOrderProducts(orderId)));

    }

    /**
     * 
     * @param createOrderRequest
     * @return
     */

    public ResponseEntity<?> createOrder(CreateOrderRequest createOrderRequest) {
        Customer customer = customerRepository.findById(tokenUtil.getUserId()).orElseThrow(
                () -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));

        Address address = new Address(createOrderRequest.getAddress());
        addressRepository.save(address);
        Order order = new Order();
        orderRepository.save(order);

        order.setOrder(customer, address, addOrderProductsToOrder(createOrderRequest.getProducts(), order));
        orderRepository.save(order);

        return ResponseEntity.ok("Order created successfully");
    }

    /***
     * 
     * @param orderProducts
     * @param order
     * @return
     */
    public List<OrderProduct> addOrderProductsToOrder(List<OrderProductDao> orderProducts, Order order) {
        try {
            List<OrderProduct> products = new ArrayList<>();
            for (OrderProductDao orderProductDao : orderProducts) {
                Product product = productRepository.findById(orderProductDao.getProductId())
                        .orElseThrow(() -> new CustomException("Product not found", HttpStatus.NOT_FOUND));
                if (product.getQuantity() < orderProductDao.getQuantity()) {
                    throw new CustomException("Product quantity is not available", HttpStatus.BAD_REQUEST);
                }
                OrderProduct orderProduct = new OrderProduct(productRepository.findById(orderProductDao.getProductId())
                        .orElseThrow(() -> new CustomException("Product not found", HttpStatus.NOT_FOUND)),
                        orderProductDao.getQuantity());

                product.setQuantity(product.getQuantity() - orderProductDao.getQuantity());
                productRepository.save(product);
                orderProduct.setOrder(order);
                // orderProductRepostory.save(orderProduct);
                products.add(orderProduct);
            }
            orderProductRepostory.saveAll(products);
            return products;
        } catch (Exception e) {
            orderRepository.delete(order);
            throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * 
     * @param orderId
     * @return
     */
    public ResponseEntity<?> deleteOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new CustomException("Order is already delivered", HttpStatus.BAD_REQUEST);

        }
        orderRepository.delete(order);
        return ResponseEntity.ok("Order deleted successfully");
    }

    /**
     * 
     * @param orderId
     * @param createOrderRequest
     * @return
     */
    public ResponseEntity<?> updateOrder(Integer orderId, CreateOrderRequest createOrderRequest) {

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new CustomException("Order is already delivered", HttpStatus.BAD_REQUEST);

        }
        Address address = new Address(createOrderRequest.getAddress());
        addressRepository.save(address);
        order.setAddress(address);
        order.setProducts(addOrderProductsToOrder(createOrderRequest.getProducts(), order));
        orderRepository.save(order);
        return ResponseEntity.ok(new OrderDto(order, orderRepository.findOrderProducts(orderId)));
    }

    /**
     * 
     * @param orderId
     * @return
     */

    public ResponseEntity<?> cancleOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new CustomException("Order is already delivered", HttpStatus.BAD_REQUEST);

        }
        order.setStatus(OrderStatus.CANCELLED);
        for (OrderProduct orderProduct : orderRepository.findOrderProducts(orderId)) {
            Product product = productRepository.findById(orderProduct.getProduct().getId()).orElseThrow(
                    () -> new CustomException("Product not found", HttpStatus.NOT_FOUND));
            product.setQuantity(product.getQuantity() + orderProduct.getQuantity());
            productRepository.save(product);
        }
        orderRepository.save(order);
        return ResponseEntity.ok("Order canceled successfully");
    }

    /*****
     * 
     * @return
     */

    public ResponseEntity<?> getPendingOrders() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.PENDING);
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            orderDtos.add(new OrderDto(order, orderRepository.findOrderProducts(order.getId())));
        }
        return ResponseEntity.ok(orderDtos);
    }

    /****
     * 
     * @return
     */

    public ResponseEntity<?> getDeliveredOrders() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.DELIVERED);
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            orderDtos.add(new OrderDto(order, orderRepository.findOrderProducts(order.getId())));
        }
        return ResponseEntity.ok(orderDtos);
    }

    /****
     * 
     * @return
     */
    public ResponseEntity<?> getCancelledOrders() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.CANCELLED);
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            orderDtos.add(new OrderDto(order, orderRepository.findOrderProducts(order.getId())));
        }
        return ResponseEntity.ok(orderDtos);
    }

    /**
     * 
     * @return
     */
    public ResponseEntity<?> getShippedOrders() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.SHIPPED);
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            orderDtos.add(new OrderDto(order, orderRepository.findOrderProducts(order.getId())));
        }
        return ResponseEntity.ok(orderDtos);
    }

    /***
     * 
     * @return
     */

    public ResponseEntity<?> getReturnedOrders() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.RETURNED);
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            orderDtos.add(new OrderDto(order, orderRepository.findOrderProducts(order.getId())));
        }
        return ResponseEntity.ok(orderDtos);
    }

    /***
     * 
     * @param orderId
     * @param status
     * @return
     */

    public ResponseEntity<?> changeOrderStatus(Integer orderId, OrderStatus status) {

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
        order.setStatus(status);
        orderRepository.save(order);
        return ResponseEntity.ok("Order status changed successfully");
    }

}
