package com.etextile.TextileECommerce.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.etextile.TextileECommerce.Entities.Address;
import com.etextile.TextileECommerce.Entities.Users;
import com.etextile.TextileECommerce.Repositories.UserRepo;

@Service
public class UserService {
    @Autowired
    UserRepo ur;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private AmazonSesClient amazonSesClient;

    public boolean adduser(Users u) {
        List<Users> lu = ur.findAll();
        for (Users u1 : lu) {
            if (u1.getEmail().equals(u.getEmail())) {
                return false;
            }
        }
        ur.save(u);
        return true;
    }

    public Users validateUser(String email, String password) {
        Users user = ur.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public void sendResetLink(Users user, String token) {
        String resetUrl = "https://chaitanyadev.shop/user/reset-password?token=" + token;
        String subject = "Password Reset Request";
        String message = "Click the link below to reset your password:\n" + resetUrl
                + "\n<<<<<<<<<<<<<<<<<   Confidential   >>>>>>>>>>>>>>>>";

        // Send the email using SES
        amazonSesClient.sendEmail("no-reply-password-forgot@chaitanyadev.shop", user.getEmail(), subject, message);

    }

    public Users findByResetToken(String token) {
        return ur.findByResetToken(token);
    }

    public void updatePassword(Users user, String newPassword) {
        user.setPassword(newPassword);
        user.setResetToken(null); // Clear the token after password reset
        ur.save(user);
    }

    public Users getUserById(String id) {
        return ur.getUserById(id);
    }

    public void saveuser(Users u) {
        ur.save(u);
    }

    public List<Address> getUserAddresses(Users user) {

        return user.getAddresses();
    }

    public List<Users> getAllUsers() {
        return ur.findAll();
    }

    public void deleteUserById(String id) {
        ur.deleteUserById(id);
    }

    public void deleteAddressByIndex(String userId, int idx) {
        Users user = ur.getUserById(userId);
        if (user != null) {
            user.removeAddress(idx);
            ur.save(user); // Save the updated user document
        }
    }

}
