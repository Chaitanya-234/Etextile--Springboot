package com.etextile.Textile_eCommerce.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.etextile.Textile_eCommerce.Entities.Users;

@Repository
public interface UserRepository extends MongoRepository<Users, String> {

    List<Users> findAllByUsername(String username); // Returns a list of users

    Users findByUsername(String usern);

    Optional<Users> findById(String id);

    Users findByEmail(String email);
    // You can define custom query methods here if needed
}