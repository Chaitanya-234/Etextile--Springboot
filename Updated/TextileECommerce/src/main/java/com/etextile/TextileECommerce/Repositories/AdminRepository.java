package com.etextile.TextileECommerce.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.etextile.TextileECommerce.Entities.Admin;

public interface AdminRepository extends MongoRepository<Admin, String> {

    public Admin findByEmail(String email);
}
