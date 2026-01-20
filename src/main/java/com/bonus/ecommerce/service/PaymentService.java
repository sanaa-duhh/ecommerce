package com.bonus.ecommerce.service;

import com.bonus.ecommerce.dto.PaymentWebhookRequest;
import com.bonus.ecommerce.model.Order;
import com.bonus.ecommerce.model.Payment;
import com.bonus.ecommerce.repository.OrderRepository;
import com.bonus.ecommerce.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    // We use this to call our own webhook
    private final RestTemplate restTemplate = new RestTemplate();

    public Payment createPayment(String orderId, Double amount) {
        // 1. Validate Order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!"CREATED".equals(order.getStatus())) {
            throw new RuntimeException("Order is not in a valid state for payment");
        }

        // 2. Create Initial Payment (PENDING)
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setStatus("PENDING");
        payment.setPaymentId("pay_" + UUID.randomUUID().toString()); // Mock external ID
        payment.setCreatedAt(Instant.now());

        Payment savedPayment = paymentRepository.save(payment);

        // 3. SIMULATE EXTERNAL BANK (The 3-second delay)
        // We run this in a background thread so the user gets an immediate response
        CompletableFuture.runAsync(() -> {
            try {
                // Wait 3 seconds
                Thread.sleep(3000);

                // Prepare Webhook Payload
                PaymentWebhookRequest webhookPayload = new PaymentWebhookRequest();
                webhookPayload.setPaymentId(savedPayment.getId());
                webhookPayload.setOrderId(orderId);
                webhookPayload.setStatus("SUCCESS");

                // Call the Webhook (Loopback to our own app)
                restTemplate.postForEntity(
                        "http://localhost:8080/api/webhooks/payment",
                        webhookPayload,
                        String.class
                );

                System.out.println("Mock Payment Completed for Order: " + orderId);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return savedPayment;
    }
}