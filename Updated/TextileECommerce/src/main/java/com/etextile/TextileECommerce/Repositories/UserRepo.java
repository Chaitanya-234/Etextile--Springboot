package com.etextile.TextileECommerce.Repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.etextile.TextileECommerce.Entities.Users;

public interface UserRepo extends MongoRepository<Users, String> {

    Optional<Users> findByEmailAndMobile(String email, Long mobile);

    Users findByEmail(String email);

    Users findByResetToken(String resetToken);

    Users getUserById(String id);

    void deleteUserById(String id);

}
