package com.etextile.TextileECommerce.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etextile.TextileECommerce.Entities.Products;
import com.etextile.TextileECommerce.Repositories.ProductRepo;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepository;

    public List<Products> getAllProducts() {
        return productRepository.findAll();
    }

    public Products getProductById(String id) {
        Optional<Products> product = productRepository.findById(id);
        return product.orElse(null);
    }

    public void addProduct(Products product) {
        productRepository.save(product);
    }

    public Products updateProduct(String id, Products productDetails) {
        Products product = productRepository.getProductsById(id);
        if (product != null) {
            product.setTitle(productDetails.getTitle());
            product.setDescription(productDetails.getDescription());
            product.setCategory(productDetails.getCategory());
            product.setPrice(productDetails.getPrice());
            product.setStock(productDetails.getStock());
            product.setImage(productDetails.getImage());
            product.setDiscount(productDetails.getDiscount());
            product.setDiscountPrice(productDetails.getDiscountPrice());
            product.setIsActive(productDetails.getIsActive());
            return productRepository.save(product);
        }
        return null;
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    public void deleteById(String id) {
        productRepository.deleteById(id);
    }

    public List<Products> searchProductss(String query) {
        return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(query, query);
    }

    public List<Products> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

}
