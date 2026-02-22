package com.etextile.TextileECommerce.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etextile.TextileECommerce.Entities.Admin;
import com.etextile.TextileECommerce.Repositories.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin validateAdmin(String email, String password) {
        Admin ad = adminRepository.findByEmail(email);

        if (ad != null && ad.getPassword().equals(password)) {

            return ad;
        }
        return null;
    }

}
