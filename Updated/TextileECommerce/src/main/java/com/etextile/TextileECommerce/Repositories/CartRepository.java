package com.etextile.TextileECommerce.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.etextile.TextileECommerce.Entities.Cart;
import com.etextile.TextileECommerce.Entities.Products;
import com.etextile.TextileECommerce.Entities.Users;

public interface CartRepository extends MongoRepository<Cart, String> {
    public Cart findByProductIdAndUserId(String productId, String userId);

    public Integer countByUserId(String userId);

    public List<Cart> findByUserId(String userId);

    List<Cart> findByUser(Users user);

    Optional<Cart> findByUserAndProduct(Users user, Products product);

    Cart findByUserIdAndProductId(String userId, String productId);

    void deleteAllByUser(Users user);

}
