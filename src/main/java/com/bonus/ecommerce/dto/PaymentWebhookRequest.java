package com.bonus.ecommerce.dto;

import lombok.Data;

@Data
public class PaymentWebhookRequest {
    private String paymentId;
    private String orderId;
    private String status; // "SUCCESS" or "FAILED"
}