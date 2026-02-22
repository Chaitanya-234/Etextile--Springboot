package com.etextile.TextileECommerce.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.etextile.TextileECommerce.Entities.OrderRequest;

public interface OrderRepository extends MongoRepository<OrderRequest, String> {

    List<OrderRequest> findByUserId(String userId);

}
