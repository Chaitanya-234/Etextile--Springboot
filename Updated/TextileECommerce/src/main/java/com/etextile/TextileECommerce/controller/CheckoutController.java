package com.etextile.TextileECommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.etextile.TextileECommerce.Entities.Cart;
import com.etextile.TextileECommerce.Entities.Products;
import com.etextile.TextileECommerce.Entities.Users;
import com.etextile.TextileECommerce.Service.CartService;
import com.etextile.TextileECommerce.Service.ProductService;
import com.etextile.TextileECommerce.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("loggedInUser")
public class CheckoutController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    private Users getCurrentUserFromSession(HttpSession session) {
        return (Users) session.getAttribute("loggedInUser");
    }

    @GetMapping("/checkout/{productId}")
    public String checkout(@PathVariable("productId") String productId, Model model, HttpSession session) {
        Users currentUser = getCurrentUserFromSession(session);

        // Check if the user is logged in
        if (currentUser == null) {
            return "redirect:/login";
        }

        // Fetch the product by ID
        Products product = productService.getProductById(productId);

        // Add product to the model
        model.addAttribute("product", product);
        model.addAttribute("user", currentUser);
        model.addAttribute("userAddresses", currentUser.getAddresses());

        return "checkout";
    }

    @GetMapping("/checkout/cart/{userId}")
    public String checkoutPage(@PathVariable("userId") String userId, Model model, HttpSession session) {

        Users currentUser = getCurrentUserFromSession(session);

        // Check if the user is logged in
        if (currentUser == null) {
            return "redirect:/login";
        }

        Users user = userService.getUserById(userId);
        List<Cart> cartItems = cartService.getCartItemsByUser(user);
        double totalOrderPrice = cartService.calculateTotalOrderPrice(cartItems);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalOrderPrice);
        model.addAttribute("user", user);
        model.addAttribute("userAddresses", user.getAddresses());

        return "cartchekout";
    }
}
