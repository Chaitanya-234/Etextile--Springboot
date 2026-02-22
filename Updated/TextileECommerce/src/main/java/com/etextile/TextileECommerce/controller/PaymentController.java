package com.etextile.TextileECommerce.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.etextile.TextileECommerce.Entities.Address;
import com.etextile.TextileECommerce.Entities.Cart;
import com.etextile.TextileECommerce.Entities.OrderRequest;
import com.etextile.TextileECommerce.Entities.Users;
import com.etextile.TextileECommerce.Service.AmazonSesClient;
import com.etextile.TextileECommerce.Service.CartService;
import com.etextile.TextileECommerce.Service.OrderService;
import com.etextile.TextileECommerce.Service.UserService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    private RazorpayClient razorpayClient;
    @Autowired
    private AmazonSesClient amazonSesClient;

    public PaymentController() throws RazorpayException {
        // Initialize the Razorpay Client with your API Key and Secret

        this.razorpayClient = new RazorpayClient("rzp_test_EhmV4fSlhCaBkz", "AZsOEbUdcCI4XimLqPnnHYPk");
    }

    @PostMapping("/razorpay")
    public String paymentPage(@ModelAttribute OrderRequest orderRequest, HttpSession session,
            @RequestParam("selectedAddress") int selectedAddressIndex, @RequestParam("meters") int meters,
            Model model) {
        Users user = (Users) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        // Retrieve selected address
        Address selectedAddress = user.getAddresses().get(selectedAddressIndex);

        // Calculate total price based on meters
        double totalPrice = meters * orderRequest.getTotalPrice();
        orderRequest.setTotalPrice(totalPrice);
        orderRequest.setQuantity(meters);
        orderRequest.setAddress(selectedAddress);
        orderRequest.setOrderDate();

        try {
            // Razorpay client initialization
            this.razorpayClient = new RazorpayClient("rzp_test_JfT5Na6ZsaWOpl", "utAdW4xgbXIn79dm72gNOd2L");

            // Create a new Razorpay order
            JSONObject orderRequestj = new JSONObject();
            orderRequestj.put("amount", totalPrice * 100); // Amount in paise (₹500 = 50000 paise)
            orderRequestj.put("currency", "INR");
            orderRequestj.put("receipt", "order_rcptid_11");

            // Create order in Razorpay
            Order order = razorpayClient.orders.create(orderRequestj);
            System.out.println(order);

            // Add necessary model attributes
            model.addAttribute("razorpayOrderId", order.get("id")); // Order ID from Razorpay
            model.addAttribute("orderreq", orderRequest);
            model.addAttribute("totalPrice", totalPrice); // Total price to display
            model.addAttribute("user", user); // User details for prefill
            model.addAttribute("selectedAddress", selectedAddress); // Selected address for order
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions and return an error page if necessary
            model.addAttribute("error", "Payment initiation failed. Please try again.");
            return "error";
        }

        // Return the payment page to initiate the payment
        return "razorpay";
    }

    @PostMapping("/success")
    public String paymentSuccess(Model model, HttpSession session,
            @ModelAttribute OrderRequest request) {
        // Handle the payment success logic (save order details, etc.)
        Users user = (Users) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        Address address = new Address();
        address.setStreet(request.getAddress().getStreet());
        address.setCity(request.getAddress().getCity());
        address.setState(request.getAddress().getState());
        address.setZipCode(request.getAddress().getZipCode());
        address.setCountry(request.getAddress().getCountry());
        address.setLabel(request.getAddress().getLabel());

        request.setAddress(address);
        request.setName(user.getFullName());

        // Create and save the order based on the successful payment
        OrderRequest savedOrder = orderService.saveOrder(request); // This should be the same as before
        String subject = "Order Confirmation";
        String content = "Dear " + user.getFullName() + ",\n\nThank you for your order!\n\nOrder Details:\n" +
                "Product: " + savedOrder.getProductName() + "\n" +
                "Quantity: " + savedOrder.getQuantity() + "\n" +
                "Total Price: " + savedOrder.getTotalPrice() + "\n\n" +
                "Delivery Address: " + savedOrder.getAddress() + "\n" +
                "Order Date: " + savedOrder.getOrderDate();
        amazonSesClient.sendEmail("etextile-order@chaitanyadev.shop", user.getEmail(), subject, content);
        // emailService.sendEmail(user.getEmail(), subject, content);
        model.addAttribute("order", savedOrder);
        return "order-confirm";
    }

    @PostMapping
    public String processPayment(@RequestParam("selectedAddress") int selectedAddressIndex, Model model,
            HttpSession session) {

        Users curuser = (Users) session.getAttribute("loggedInUser");
        Address selectedAddress = userService.getUserAddresses(curuser).get(selectedAddressIndex);

        model.addAttribute("selectedAddress", selectedAddress);
        model.addAttribute("totalprice", selectedAddress);

        return "payment";
    }

    @PostMapping("/cart/rpay")
    public String paymentPage(@RequestParam("selectedAddress") int addressIndex, HttpSession session, Model model)
            throws RazorpayException {
        Users user = (Users) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        List<Cart> cartItems = cartService.getCartItemsByUser(user);
        double totalOrderPrice = cartService.calculateTotalOrderPrice(cartItems);

        // Save the payment order in the session (or DB if needed)

        session.setAttribute("cartItems", cartItems);

        // Initialize Razorpay payment order
        RazorpayClient razorpay = new RazorpayClient("rzp_test_JfT5Na6ZsaWOpl", "utAdW4xgbXIn79dm72gNOd2L");
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (totalOrderPrice * 100)); // Amount in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_123456");

        Order order = razorpay.orders.create(orderRequest);

        model.addAttribute("razorpayOrderId", order.get("id"));
        model.addAttribute("totalOrderPrice", totalOrderPrice);
        model.addAttribute("user", user);
        model.addAttribute("addidx", addressIndex);
        return "cart_razorpay";
    }

    @PostMapping("/cartpay")
    public String cartpay(@RequestParam("selectedAddress") int addressIndex, @ModelAttribute OrderRequest orderRequest,
            HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        List<Cart> cartItems = cartService.getCartItemsByUser(user);
        List<OrderRequest> currentOrders = orderService.cartpaym(cartItems, user, addressIndex);
        double totalOrderPrice = cartService.calculateTotalOrderPrice(cartItems);
        model.addAttribute("userOrders", currentOrders);
        model.addAttribute("totalorderprice", totalOrderPrice);
        cartService.clearCart(user);
        String subject = "Order Confirmation for Cart Items";
        StringBuilder content = new StringBuilder(
                "Dear " + user.getFullName() + ",\n\nThank you for your cart order!\n\nOrder Details:\n");
        for (Cart cart : cartItems) {
            content.append("Product: ").append(cart.getProduct().getTitle()).append("\n")
                    .append("Quantity: ").append(cart.getQuantity()).append("\n")
                    .append("Price: ").append(cart.getProduct().getDiscountPrice() * cart.getQuantity()).append("\n\n");
        }
        content.append("Total Order Price: ").append(totalOrderPrice).append("\n\n")
                .append("Delivery Address: ").append(user.getAddresses().get(addressIndex));

        // Send the email using SES
        // emailService.sendEmail(user.getEmail(), subject, content.toString());
        amazonSesClient.sendEmail("etextile-order@chaitanyadev.shop", user.getEmail(), subject, content.toString());

        return "cartconfirm";
    }

}
