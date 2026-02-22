package com.etextile.TextileECommerce.Entities;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Embedded;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@Document
public class OrderRequest {

    @Id
    public String id;

    private String name;
    private String email;

    private String mobileNo;

    @Embedded
    private Address address;
    private String userId;
    private String productId;
    private String paymenttype;
    private Double totalPrice;
    private LocalDate orderDate;
    private String productName;
    private String image;
    private int quantity;
    private String status;
    private String razorpayid;

    public void setOrderDate() {
        orderDate = LocalDate.now();
    }
}