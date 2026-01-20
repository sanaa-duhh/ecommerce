package com.bonus.ecommerce.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "cart_items")
public class CartItem {
    @Id
    private String id;
    private String userId;      // "Foreign Key" to User
    private String productId;   // "Foreign Key" to Product
    private Integer quantity;
}