package com.etextile.TextileECommerce.Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;

@Document
public class Users {

    @Id
    public String id;
    public String email;
    public String password;
    public String mobile;
    public Set<String> roles;
    private String fullName;
    @ElementCollection
    @Embedded
    private List<Address> addresses = new ArrayList<>();
    private String resetToken;
    private String profilePictureUrl;

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Users(String id, String email, String password, Set<String> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public Users() {
        this.addresses = new ArrayList<>(); // Initialize in no-arg constructor
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void removeAddress(int idx) {
        if (idx >= 0 && idx < this.addresses.size()) {
            this.addresses.remove(idx);
        }
    }

    @Override
    public String toString() {
        return "Users [id=" + id + ", email=" + email + ", password=" + password + ", mobile=" + mobile + ", roles="
                + roles + ", fullName=" + fullName + ", addresses=" + addresses + "]";
    }

}
