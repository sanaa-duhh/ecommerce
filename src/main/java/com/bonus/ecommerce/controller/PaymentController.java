package com.bonus.ecommerce.controller;

import com.bonus.ecommerce.dto.PaymentRequest;
import com.bonus.ecommerce.model.Payment;
import com.bonus.ecommerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.createPayment(
                request.getOrderId(),
                request.getAmount()
        ));
    }
}