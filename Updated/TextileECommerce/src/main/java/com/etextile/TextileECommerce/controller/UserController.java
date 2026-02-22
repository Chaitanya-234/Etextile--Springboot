package com.etextile.TextileECommerce.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.etextile.TextileECommerce.Entities.Address;
import com.etextile.TextileECommerce.Entities.OrderRequest;
import com.etextile.TextileECommerce.Entities.Products;
import com.etextile.TextileECommerce.Entities.Users;
import com.etextile.TextileECommerce.Repositories.ProductRepo;
import com.etextile.TextileECommerce.Repositories.UserRepo;
import com.etextile.TextileECommerce.Service.AmazonSesClient;
import com.etextile.TextileECommerce.Service.CloudinaryService;
import com.etextile.TextileECommerce.Service.OrderService;
import com.etextile.TextileECommerce.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService use;

    @Autowired
    UserRepo ur;
    @Autowired
    OrderService orderService;
    @Autowired
    ProductRepo productRepository;
    @Autowired
    private AmazonSesClient amazonSesClient;
    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/reguser")
    public String registerUser(@ModelAttribute Users user, HttpSession session,
            @RequestParam("profilePicture") MultipartFile profilePicture, Model model) throws IOException {
        if (!use.adduser(user)) {
            return "redirect:/user/fail";
        }
        String imageUrl = cloudinaryService.uploadImage(profilePicture);
        user.setProfilePictureUrl(imageUrl);
        String subj = "Welcome to Etextile,😎😎";
        String msg = "Thank you for registering with us. Your account has been created successfully. Please login"
                + "/n _______________________________" + "/n   " + "The Fabric which makes your Choice Great";
        amazonSesClient.sendEmail("etextile@chaitanyadev.shop", user.getEmail(), subj, msg);
        ur.save(user);
        return "redirect:/user/success";

    }

    @GetMapping("/fail")
    public String failure() {
        return "fail";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @PostMapping("/validateu")
    public String validate(@RequestParam("email") String email, @RequestParam("password") String password, Model model,
            HttpSession session) {
        Users user = use.validateUser(email, password); // Delegating validation logic to service layer
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            model.addAttribute("user", user);
            return "redirect:/home";
        } else {
            model.addAttribute("text", "Invalid Credentials");
            return "fail";
        }
    }

    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        // Get the logged-in user from the session
        Users user = (Users) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "profile"; // Serve the profile page
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalidate the session to log the user out
        session.invalidate();
        // Redirect to the home or login page after logout
        return "redirect:/home"; // You can change this to home or any other page
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        Users user = ur.findByEmail(email);
        if (user == null) {
            model.addAttribute("errorMessage", "No account found with that email.");
            return "forgot-password";
        }

        // Generate reset token and save it to the user
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        ur.save(user);

        use.sendResetLink(user, token);

        model.addAttribute("successMessage", "Password reset link sent to your email.");
        return "forgot-password";
    }

    // Show password reset form
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam(value = "token", required = false) String token, Model model) {
        Users user = use.findByResetToken(token);
        if (user == null) {
            model.addAttribute("errorMessage", "Invalid or expired token.");
            return "error";
        }
        model.addAttribute("token", token);
        return "reset-password";
    }

    // Handle password reset form submission
    @PostMapping("/reset-password")
    public String handlePasswordReset(@RequestParam("token") String token,
            @RequestParam("password") String newPassword, Model model) {
        Users user = use.findByResetToken(token);
        if (user == null) {
            model.addAttribute("errorMessage", "Invalid or expired token.");
            return "error";
        }

        // Update the user's password
        use.updatePassword(user, newPassword);
        model.addAttribute("successMessage", "Your password has been reset successfully.");
        return "login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/add-addresss")
    public String addressaddd(@ModelAttribute("newAddress") Address newAddress, HttpSession session) {
        Users user = (Users) session.getAttribute("loggedInUser");
        if (user != null) {
            user.getAddresses().add(newAddress);
            use.saveuser(user);
            return "redirect:/checkout/cart/" + user.getId();
        }
        return "redirect:/home";

    }

    @GetMapping("/delete-add/{index}")
    public String remove_add(@PathVariable("index") String idx, HttpSession session) {
        Users user = (Users) session.getAttribute("loggedInUser");
        if (user != null) {
            user.getAddresses().remove(Integer.parseInt(idx));
            use.saveuser(user);
            return "redirect:/checkout/cart/" + user.getId();
        }

        return "redirect:/home";

    }

    @PostMapping("/add-address")
    public String addressadd(@ModelAttribute("newAddress") Address newAddress, HttpSession session,
            @RequestParam("productId") String pid) {
        Users user = (Users) session.getAttribute("loggedInUser");
        if (user != null) {
            user.getAddresses().add(newAddress);
            use.saveuser(user);
            return "redirect:/checkout/" + pid;
        }

        return "redirect:/home";

    }

    @GetMapping("/delete-address/{idx}")
    public String removeAddress(@PathVariable("idx") int idx, HttpSession session,
            @RequestParam("productId") String productId) {
        Users user = (Users) session.getAttribute("loggedInUser");
        if (user != null && idx >= 0 && idx < user.getAddresses().size()) {
            user.getAddresses().remove(idx);
            use.saveuser(user);

            return "redirect:/checkout/" + productId; // Redirect to checkout page with the product ID
        }
        return "redirect:/home"; // Redirect to home page if the user is not found
    }

    @GetMapping("/myorders")
    public String myorders(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        List<OrderRequest> userOrders = orderService.getOrdersByUserId(user.getId());
        for (OrderRequest order : userOrders) {
            Optional<Products> productOptional = productRepository.findById(order.getProductId());
            productOptional.ifPresent(product -> order.setImage(product.getImage()));
            orderService.saveOrder(order);
        }

        model.addAttribute("orders", userOrders);
        model.addAttribute("orders", userOrders);

        return "myorder";
    }

}
