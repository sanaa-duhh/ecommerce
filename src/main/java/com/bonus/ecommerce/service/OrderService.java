package com.bonus.ecommerce.service;

import com.bonus.ecommerce.model.*;
import com.bonus.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional // Ensures all steps succeed or fail together
    public Order createOrder(String userId) {
        // 1. Get Cart Items
        List<CartItem> cartItems = cartRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double totalAmount = 0.0;

        // 2. Calculate Total & Check Stock
        for (CartItem item : cartItems) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            totalAmount += (product.getPrice() * item.getQuantity());
        }

        // 3. Create Order
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("CREATED");
        order.setTotalAmount(totalAmount);
        order.setCreatedAt(Instant.now());
        order = orderRepository.save(order);

        // 4. Create OrderItems & Deduct Stock
        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId()).get();

            // Create OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice()); // Freeze the price at time of order
            orderItemRepository.save(orderItem);

            // Deduct Stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }

        // 5. Clear Cart
        cartRepository.deleteByUserId(userId);

        return order;
    }

    public Order getOrder(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}