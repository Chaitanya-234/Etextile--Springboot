package com.etextile.TextileECommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.etextile.TextileECommerce.Entities.Cart;
import com.etextile.TextileECommerce.Entities.Products;
import com.etextile.TextileECommerce.Entities.Users;
import com.etextile.TextileECommerce.Service.CartService;
import com.etextile.TextileECommerce.Service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    // Fetch current user from session
    private Users getCurrentUserFromSession(HttpSession session) {
        return (Users) session.getAttribute("loggedInUser");
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        Users currentUser = getCurrentUserFromSession(session);

        if (currentUser == null) {
            return "redirect:/login";

        }

        // Fetch cart items for the current user
        List<Cart> cartItems = cartService.getCartItemsByUser(currentUser);

        // Calculate total order price
        Double totalOrderPrice = cartItems.stream()
                .mapToDouble(Cart::getTotalPrice)
                .sum();

        // Add cart items and total price to the model
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalOrderPrice", totalOrderPrice);

        return "cart"; // Returns the cart.html page
    }

    // Add a product to the cart
    @GetMapping("/cart/add/{productId}")
    public String addToCart(@PathVariable("productId") String productId,
            @RequestParam("quantity") Integer quantity,
            HttpSession session) {
        Users currentUser = getCurrentUserFromSession(session);

        if (currentUser == null) {
            return "redirect:/login"; // Redirect to login if user is not logged in
        }

        // Fetch product by id
        Products product = productService.getProductById(productId);

        // Add product to cart
        cartService.addProductToCart(currentUser, product, quantity);

        return "redirect:/cart"; // Redirect to cart page after adding the product
    }

    // Remove an item from the cart
    @PostMapping("/cart/remove/{cartId}")
    public String removeFromCart(@PathVariable("cartId") String cartId) {
        cartService.removeCartItem(cartId);
        return "redirect:/cart"; // Redirect to cart page after removing the item
    }

    @PostMapping("/cart/updateQuantity/{productId}")
    public String updateQuantity(@PathVariable("productId") String productId,
            @RequestParam("action") String action,
            HttpSession session) {
        // Retrieve the cart for the current user
        Users currentUser = getCurrentUserFromSession(session); // Assuming user is stored in session
        System.out.println(currentUser + "    ------    " + productId);
        Cart cart = cartService.getCartByUserAndProduct(currentUser.getId(), productId);
        System.out.println(cart);

        if (cart != null) {
            // Update the quantity
            cartService.updateCartQuantity(cart, action);
            session.setAttribute("cart", cart); // Update session with the new cart data
        }

        return "redirect:/cart";
    }

}
