package com.etextile.Textile_eCommerce.Entities;

import java.util.Arrays;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "Usert")
public class TempUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getImage() {
        return image;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "TempUser [id=" + id + ", username=" + username + ", image=" + Arrays.toString(image) + "]";
    }

}
