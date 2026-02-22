package com.etextile.TextileECommerce.controller;

import java.io.IOException;
import java.util.List;

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

import com.etextile.TextileECommerce.Entities.Admin;
import com.etextile.TextileECommerce.Entities.Products;
import com.etextile.TextileECommerce.Service.CloudinaryService;
import com.etextile.TextileECommerce.Service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/products")
public class ProductsController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CloudinaryService cloudinaryService;

    // Show all products page
    @GetMapping
    public String getAllProducts(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("loggedAdmin");
        if (admin == null) {
            model.addAttribute("error", "You need to login first!");
            return "redirect:/admin/adminlogin";
        }
        List<Products> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products"; // Thymeleaf view for displaying all products
    }

    // Show form for adding a new product
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Products());
        return "product-add"; // Thymeleaf view for adding a new product
    }

    // Add a new product
    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") Products product,
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        String imageUrl = cloudinaryService.uploadImage(imageFile);
        product.setImage(imageUrl);
        productService.addProduct(product);

        return "redirect:/admin/products";
    }

    // Show form for editing a product

    // Update a product
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable String id,
            @ModelAttribute("product") Products product,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        // Handle image file if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(imageFile);
            product.setImage(imageUrl); // Set the URL in the product object
        }

        // Call the service to update the product
        productService.updateProduct(id, product);

        // Redirect after successful update
        return "redirect:/admin/products";
    }

    // Delete a product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") String id) {
        // Your service logic to delete the product by ID
        productService.deleteById(id);

        // Redirect to the product management page after deletion
        return "redirect:/admin/products";
    }
}
