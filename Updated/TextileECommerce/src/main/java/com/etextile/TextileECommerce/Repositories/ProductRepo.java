package com.etextile.TextileECommerce.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.etextile.TextileECommerce.Entities.Products;

public interface ProductRepo extends MongoRepository<Products, String> {

    public Products getProductsById(String id);

    List<Products> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String title, String category);

    List<Products> findByCategory(String category);

}
