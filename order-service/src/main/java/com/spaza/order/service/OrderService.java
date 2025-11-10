package com.spaza.order.service;

import com.spaza.order.model.*;
import com.spaza.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public ReadableOrderConfirmation checkout(String cartCode, PersistableOrder order) {
        ReadableOrder newOrder = new ReadableOrder();
        newOrder.setCustomerId(order.getCustomerId());
        newOrder.setStatus("PENDING");
        newOrder.setTotal(order.getTotal());
        ReadableOrder saved = orderRepository.save(newOrder);
        return new ReadableOrderConfirmation(saved.getId(), "ORD-" + saved.getId());
    }

    public ReadableOrderConfirmation checkout(String cartCode, PersistableAnonymousOrder order) {
        ReadableOrder newOrder = new ReadableOrder();
        newOrder.setStatus("PENDING");
        newOrder.setTotal(order.getTotal());
        ReadableOrder saved = orderRepository.save(newOrder);
        return new ReadableOrderConfirmation(saved.getId(), "ORD-" + saved.getId());
    }

    public ReadableOrderList list(Integer count, Integer page) {
        List<ReadableOrder> orderList = orderRepository.findAll();
        return new ReadableOrderList(orderList, orderList.size());
    }

    public ReadableOrder getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public ReadableOrderList list(int count, String email, Long id, String name, int page, String phone, String status) {
        if (status != null) {
            List<ReadableOrder> orders = orderRepository.findByStatus(status);
            return new ReadableOrderList(orders, orders.size());
        }
        return list(count, page);
    }

    public ReadableOrderList listByCustomer(Integer count, Long customerId, Integer start) {
        List<ReadableOrder> orders = orderRepository.findByCustomerId(customerId);
        return new ReadableOrderList(orders, orders.size());
    }

    public ReadableOrder get(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void updateOrderCustomer(Long id, Customer customer) {
        orderRepository.findById(id).ifPresent(order -> {
            order.setCustomerId(customer.getId());
            orderRepository.save(order);
        });
    }

    public void updateOrderStatus(Long id, String status) {
        orderRepository.findById(id).ifPresent(order -> {
            order.setStatus(status);
            orderRepository.save(order);
        });
    }
}