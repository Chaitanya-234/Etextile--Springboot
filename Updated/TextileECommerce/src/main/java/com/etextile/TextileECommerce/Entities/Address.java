package com.etextile.TextileECommerce.Entities;

import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    private String id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String label;

    public Address() {
        this.id = UUID.randomUUID().toString(); // Assign a unique ID when the address is created
    }

    public Address(String street, String city, String state, String zipCode, String country, String label) {
        this.id = UUID.randomUUID().toString(); // Assign a unique ID
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Address [street=" + street + ", city=" + city + ", state=" + state + ", zipCode=" + zipCode
                + ", country=" + country + ", label=" + label + "]";
    }
}
