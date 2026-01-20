package com.bonus.ecommerce.controller;

import com.bonus.ecommerce.dto.PaymentWebhookRequest;
import com.bonus.ecommerce.model.Order;
import com.bonus.ecommerce.model.Payment;
import com.bonus.ecommerce.repository.OrderRepository;
import com.bonus.ecommerce.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/payment")
    public ResponseEntity<String> handlePaymentWebhook(@RequestBody PaymentWebhookRequest payload) {
        // 1. Find the Payment
        Payment payment = paymentRepository.findById(payload.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // 2. Update Payment Status
        payment.setStatus(payload.getStatus());
        paymentRepository.save(payment);

        // 3. Update Order Status
        Order order = orderRepository.findById(payload.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if ("SUCCESS".equals(payload.getStatus())) {
            order.setStatus("PAID");
        } else {
            order.setStatus("PAYMENT_FAILED");
        }
        orderRepository.save(order);

        return ResponseEntity.ok("Webhook processed");
    }
}