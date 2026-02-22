package com.etextile.TextileECommerce.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etextile.TextileECommerce.Entities.Address;
import com.etextile.TextileECommerce.Entities.Cart;
import com.etextile.TextileECommerce.Entities.OrderRequest;
import com.etextile.TextileECommerce.Entities.Users;
import com.etextile.TextileECommerce.Repositories.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public OrderRequest saveOrder(OrderRequest orderRequest) {
        orderRequest.setOrderDate();
        return orderRepository.save(orderRequest);
    }

    public List<OrderRequest> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<OrderRequest> getAllOrders() {
        return orderRepository.findAll();
    }

    public void updateOrderStatus(String orderId, String status) {
        Optional<OrderRequest> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            OrderRequest order = orderOpt.get();
            order.setStatus(status);
            orderRepository.save(order);
        }
    }

    public void deleteOrder(String orderId) {
        orderRepository.deleteById(orderId);
    }

    public List<OrderRequest> cartpaym(List<Cart> c, Users u, int addressIndex) {
        List<OrderRequest> currentOrders = new ArrayList<>();
        for (Cart cart : c) {
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setEmail(u.getEmail());
            orderRequest.setName(u.getFullName());
            orderRequest.setOrderDate();
            orderRequest.setProductId(cart.getProduct().getId());
            orderRequest.setImage(cart.getProduct().getImage());
            orderRequest.setUserId(u.id);
            orderRequest.setQuantity(cart.getQuantity());
            orderRequest.setTotalPrice(cart.getQuantity() * cart.getProduct().getDiscountPrice());
            Address selectedAddress = u.getAddresses().get(addressIndex);
            orderRequest.setAddress(selectedAddress);
            orderRequest.setProductName(cart.getProduct().getTitle());
            orderRequest.setStatus("ordered");
            orderRepository.save(orderRequest);
            OrderRequest savedOrder = orderRepository.save(orderRequest);
            currentOrders.add(savedOrder);

        }
        return currentOrders;
    }
}
