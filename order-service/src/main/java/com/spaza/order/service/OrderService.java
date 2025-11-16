package com.spaza.order.service;

import com.spaza.order.entity.Order;
import com.spaza.order.model.*;
import com.spaza.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Value("${customer.service.url}") private String customerServiceUrl;
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    public Order save(Order order) {
        return orderRepository.save(order);
    }
    
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
    
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
    
    public List<Order> findByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
    
    public List<Order> findByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
    
    public List<Order> findByMerchantId(Long merchantId) {
        return orderRepository.findByMerchantId(merchantId);
    }
    
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
    
    public Long getCustomerIdByEmail(String email) {
        try {
            String url = customerServiceUrl + "/api/v1/private/customer/email/" + email;
            Customer customer = restTemplate.getForObject(url, Customer.class);
            return customer != null ? customer.getId() : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public ReadableOrderConfirmation createOrder(String cartCode, PersistableOrder persistableOrder, Long authenticatedCustomerId) {
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        
        try {
            String cartUrl = customerServiceUrl + "/api/v1/cart/" + cartCode;
            System.out.println("=== FETCHING CART: " + cartUrl);
            Map cartResponse = restTemplate.getForObject(cartUrl, Map.class);
            System.out.println("=== CART RESPONSE: " + cartResponse);
            
            if (cartResponse != null && cartResponse.get("total") != null) {
                total = new java.math.BigDecimal(cartResponse.get("total").toString());
                System.out.println("=== TOTAL EXTRACTED: " + total);
            } else {
                System.out.println("=== NO TOTAL IN RESPONSE");
            }
        } catch (Exception e) {
            System.err.println("=== ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        
        Order order = new Order();
        order.setCustomerId(authenticatedCustomerId);
        order.setStatus("PENDING");
        order.setTotal(total);
        order.setDateCreated(java.time.LocalDateTime.now());
        System.out.println("=== SAVING ORDER WITH TOTAL: " + total);
        
        Order saved = orderRepository.save(order);
        System.out.println("=== SAVED ORDER TOTAL: " + saved.getTotal());
        return new ReadableOrderConfirmation(saved.getId(), "ORD-" + saved.getId());
    }
    
    public ReadableOrderConfirmation createAnonymousOrder(String cartCode, PersistableAnonymousOrder order) {
        Order newOrder = new Order();
        newOrder.setStatus("PENDING");
        newOrder.setTotal(java.math.BigDecimal.ZERO);
        Order saved = orderRepository.save(newOrder);
        
        return new ReadableOrderConfirmation(saved.getId(), "ORD-" + saved.getId());
    }
    
    public ReadableOrderList getOrders(Integer count, Integer page) {
        List<Order> orders = orderRepository.findAll();
        List<ReadableOrder> readableOrders = orders.stream().map(this::toReadable).collect(Collectors.toList());
        return new ReadableOrderList(readableOrders, readableOrders.size());
    }
    
    public ReadableOrder getOrder(Long id) {
        return orderRepository.findById(id).map(this::toReadable).orElse(null);
    }
    
    public ReadableOrderList searchOrders(int count, String email, Long id, String name, int page, String phone, String status) {
        List<Order> orders = status != null ? orderRepository.findByStatus(status) : orderRepository.findAll();
        List<ReadableOrder> readableOrders = orders.stream().map(this::toReadable).collect(Collectors.toList());
        return new ReadableOrderList(readableOrders, readableOrders.size());
    }
    
    public ReadableOrderList getCustomerOrders(Integer count, Long customerId, Integer start) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        List<ReadableOrder> readableOrders = orders.stream().map(this::toReadable).collect(Collectors.toList());
        return new ReadableOrderList(readableOrders, readableOrders.size());
    }
    
    public void updateCustomer(Long orderId, Customer customer) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setCustomerId(customer.getId());
            orderRepository.save(order);
        });
    }
    
    public void updateStatus(Long orderId, String status) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(status);
            orderRepository.save(order);
        });
    }
    
    private ReadableOrder toReadable(Order order) {
        ReadableOrder readable = new ReadableOrder();
        readable.setId(order.getId());
        readable.setCustomerId(order.getCustomerId());
        readable.setStatus(order.getStatus());
        readable.setTotal(order.getTotal());
        return readable;
    }
}
