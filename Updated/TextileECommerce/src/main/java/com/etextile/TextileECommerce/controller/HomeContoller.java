package com.etextile.TextileECommerce.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.etextile.TextileECommerce.Entities.Products;
import com.etextile.TextileECommerce.Entities.Users;
import com.etextile.TextileECommerce.Service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeContoller {
    @Autowired
    ProductService productService;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("user", loggedInUser);
        }
        List<Products> products = productService.getAllProducts();
        List<Products> limitedProducts = products.stream().limit(4).collect(Collectors.toList());

        // Add attributes to the model
        model.addAttribute("limitedProducts", limitedProducts); // Limited products for hero section
        model.addAttribute("products", products); // All products for other sections
        Map<String, String> uniqueCategories = products.stream()
                .collect(Collectors.toMap(
                        Products::getCategory, // Category as key
                        Products::getImage, // Image as value
                        (existing, replacement) -> existing)); // Handle duplicates, if any

        model.addAttribute("uniqueCategories", uniqueCategories);

        return "home"; // Return the "home" Thymeleaf template
    }

    @GetMapping("/register")
    public String register() {
        return "userregister";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/category/{categoryName}")
    public String getProductsByCategory(@PathVariable("categoryName") String categoryName, Model model,
            HttpSession session) {

        // Fetch products by category
        List<Products> productsByCategory = productService.getProductsByCategory(categoryName);
        model.addAttribute("products", productsByCategory);
        model.addAttribute("categoryName", categoryName); // Add category name for display

        return "categories"; // Return the view for category-specific products
    }

    @GetMapping("/productsview/{id}")
    public String viewProduct(@PathVariable("id") String id, HttpSession session, Model model) {
        Products product = productService.getProductById(id);
        model.addAttribute("product", product);
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);

        return "view-product";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        List<Products> searchResults = productService.searchProductss(query);
        model.addAttribute("products", searchResults);
        model.addAttribute("query", query);
        return "search-res";
    }

    @GetMapping("/portfolio")
    public String about() {
        return "about";
    }

    @PostMapping("/send-message")
    public String sendMessage(@RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("message") String message,
            Model model) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(email); // Use your email
            mailMessage.setTo("chaitanyaambekar44@gmail.com"); // Where you want to receive messages
            mailMessage.setSubject("New message from " + name);
            mailMessage.setText("You have received a new message from " + name + " (" + email + "):\n\n" + message);

            // Send the email
            mailSender.send(mailMessage);

            model.addAttribute("successMessage", "Your message was sent successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "An error occurred while sending your message. Please try again.");
        }

        return "about";

    }

}
