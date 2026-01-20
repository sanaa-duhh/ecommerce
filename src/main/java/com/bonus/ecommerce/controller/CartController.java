package com.bonus.ecommerce.controller;

import com.bonus.ecommerce.dto.AddToCartRequest;
import com.bonus.ecommerce.model.CartItem;
import com.bonus.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(@RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(
                request.getUserId(),
                request.getProductId(),
                request.getQuantity()
        ));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared successfully");
    }
}