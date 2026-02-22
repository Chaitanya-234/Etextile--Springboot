package com.etextile.TextileECommerce.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "cart")
@Setter
@Getter
public class Cart {

    @Id
    private String id;
    @DBRef
    private Users user;
    @DBRef
    private Products product;
    private Integer quantity;
    @Transient
    private Double totalPrice;
    @Transient
    private Double totalOrderPrice;

    public Cart() {
    }

    public Cart(Users user, Products product, Integer quantity) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        return product.getDiscountPrice() * quantity;
    }

    public Double getTotalOrderPrice() {

        return totalOrderPrice;
    }

    public void setTotalOrderPrice(Double totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }
}
