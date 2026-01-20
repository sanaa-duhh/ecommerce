package com.bonus.ecommerce.repository;

import com.bonus.ecommerce.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}