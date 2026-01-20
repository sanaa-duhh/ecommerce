package com.bonus.ecommerce.repository;

import com.bonus.ecommerce.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    // We can add custom queries here later if needed
}