package com.etextile.Textile_eCommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.etextile.Textile_eCommerce.Entities.Users;
import com.etextile.Textile_eCommerce.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository ur;

    public ResponseEntity<?> adduser(Users u) {
        if (!checkuser(u)) {
            ur.save(u);
            return ResponseEntity.ok("User saved sucess");
        } else {
            return ResponseEntity.badRequest().body("Account Already Exist");
        }

    }

    public boolean checkuser(Users u) {
        List<Users> savedUserList = ur.findAll();
        for (Users uu : savedUserList) {
            if (u.getEmail().equals(uu.getEmail())) {
                return true;

            }
        }
        return false;

    }
}
