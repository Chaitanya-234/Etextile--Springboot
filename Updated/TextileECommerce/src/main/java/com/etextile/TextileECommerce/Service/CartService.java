package com.etextile.TextileECommerce.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etextile.TextileECommerce.Entities.Cart;
import com.etextile.TextileECommerce.Entities.Products;
import com.etextile.TextileECommerce.Entities.Users;
import com.etextile.TextileECommerce.Repositories.CartRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    // Retrieve cart items for a user
    public List<Cart> getCartItemsByUser(Users user) {
        return cartRepository.findByUser(user);
    }

    // Add product to cart
    public void addProductToCart(Users user, Products product, Integer quantity) {
        // Check if the product is already in the cart
        Optional<Cart> existingCartItem = cartRepository.findByUserAndProduct(user, product);

        if (existingCartItem.isPresent()) {
            // Update the quantity if the product already exists in the cart
            Cart cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setTotalPrice(cartItem.getQuantity() * product.getDiscountPrice());
            cartRepository.save(cartItem);
        } else {
            // Create a new cart item if the product doesn't exist in the cart
            Cart newCartItem = new Cart();
            newCartItem.setUser(user);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            newCartItem.setTotalPrice(quantity * product.getDiscountPrice());
            cartRepository.save(newCartItem);
        }
    }

    public void clearCart(Users user) {
        // Logic to remove all items from the user's cart
        cartRepository.deleteAllByUser(user);
    }

    // Remove an item from the cart
    public void removeCartItem(String cartId) {
        cartRepository.deleteById(cartId);
    }

    // Get the total order price for all items in the user's cart
    public Double calculateTotalOrderPrice(List<Cart> cartItems) {
        return cartItems.stream()
                .mapToDouble(Cart::getTotalPrice)
                .sum();
    }

    public Cart getCartByUserAndProduct(String userId, String productId) {
        return cartRepository.findByUserIdAndProductId(userId, productId);
    }

    // Update cart quantity based on action (increase or decrease)
    public void updateCartQuantity(Cart cart, String action) {
        if ("increase".equals(action)) {
            cart.setQuantity(cart.getQuantity() + 1);
        } else if ("decrease".equals(action) && cart.getQuantity() > 1) {
            cart.setQuantity(cart.getQuantity() - 1);
        }

        // Save the updated cart back to MongoDB
        cartRepository.save(cart);
    }

}
