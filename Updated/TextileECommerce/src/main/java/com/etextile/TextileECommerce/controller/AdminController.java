package com.etextile.TextileECommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.etextile.TextileECommerce.Entities.Admin;
import com.etextile.TextileECommerce.Entities.OrderRequest;
import com.etextile.TextileECommerce.Service.AdminService;
import com.etextile.TextileECommerce.Service.OrderService;
import com.etextile.TextileECommerce.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Check if the admin is logged in
        Admin admin = (Admin) session.getAttribute("loggedAdmin");

        if (admin == null) {

            // Redirect to login page if not logged in
            model.addAttribute("error", "You need to login first!");
            return "redirect:/admin/adminlogin";
        }
        return "admindasboard";
    }

    @GetMapping("/adminlogin")
    public String alogin() {
        return "adminlogin";
    }

    @PostMapping("/validatea")
    public String adminval(@RequestParam("email") String email, @RequestParam("password") String password, Model model,
            HttpSession session) {
        Admin adm = adminService.validateAdmin(email, password);
        if (adm != null) {
            session.setAttribute("loggedAdmin", adm);
            return "redirect:/admin/dashboard";

        }
        model.addAttribute("error", "Invalid email or password");
        return "adminlogin";

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();
        return "redirect:/admin/adminlogin";
    }

    @GetMapping("users")
    public String manageUsers(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("loggedAdmin");
        if (admin == null) {

            // Redirect to login page if not logged in
            model.addAttribute("error", "You need to login first!");
            return "redirect:/admin/adminlogin";
        }
        model.addAttribute("users", userService.getAllUsers());
        return "userdetail"; // Return the Thymeleaf template
    }

    // Delete user by ID
    @RequestMapping(value = "/users/delete/{id}", method = RequestMethod.GET)
    public String deleteUser(@PathVariable("id") String id, HttpSession session, Model model) {

        Admin admin = (Admin) session.getAttribute("loggedAdmin");
        if (admin == null) {

            // Redirect to login page if not logged in
            model.addAttribute("error", "You need to login first!");
            return "redirect:/admin/adminlogin";
        }
        userService.deleteUserById(id);
        return "redirect:/admin/users   "; // Redirect back to the users page after deletion
    }

    @GetMapping("orders")
    public String viewOrders(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("loggedAdmin");
        if (admin == null) {

            // Redirect to login page if not logged in
            model.addAttribute("error", "You need to login first!");
            return "redirect:/admin/adminlogin";
        }
        List<OrderRequest> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "mngusrorder";
    }

    @PostMapping("/orders/update/{orderId}")
    public String updateOrderStatus(@PathVariable("orderId") String orderId, @RequestParam("status") String status,
            HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("loggedAdmin");
        if (admin == null) {

            // Redirect to login page if not logged in
            model.addAttribute("error", "You need to login first!");
            return "redirect:/admin/adminlogin";
        }
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/admin/orders";
    }

    @PostMapping("/orders/delete/{orderId}")
    public String deleteOrder(@PathVariable("orderId") String orderId, HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("loggedAdmin");
        if (admin == null) {

            // Redirect to login page if not logged in
            model.addAttribute("error", "You need to login first!");
            return "redirect:/admin/adminlogin";
        }
        orderService.deleteOrder(orderId);
        return "redirect:/admin/orders";

    }

}
