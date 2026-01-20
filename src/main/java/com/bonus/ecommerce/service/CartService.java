package com.bonus.ecommerce.service;

import com.bonus.ecommerce.model.CartItem;
import com.bonus.ecommerce.model.Product;
import com.bonus.ecommerce.repository.CartRepository;
import com.bonus.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    public CartItem addToCart(String userId, String productId, Integer quantity) {
        // 1. Validate Product Exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 2. Check Stock
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock available");
        }

        // 3. Check if item already in cart (Smart Add)
        Optional<CartItem> existingItem = cartRepository.findByUserIdAndProductId(userId, productId);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setUserId(userId);
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            return cartRepository.save(newItem);
        }
    }

    public List<CartItem> getCart(String userId) {
        return cartRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }
}