package com.spaza.order.delegate;

import com.spaza.order.model.*;
import com.spaza.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderApiCtrlDelegateImpl implements OrderApiCtrlDelegate {

    @Autowired
    private OrderService orderService;

    @Override
    public ResponseEntity<ReadableOrderConfirmation> checkout(String code, PersistableOrder order) {
        ReadableOrderConfirmation confirmation = orderService.checkout(code, order);
        return ResponseEntity.ok(confirmation);
    }

    @Override
    public ResponseEntity<ReadableOrderConfirmation> checkout(String code, PersistableAnonymousOrder order) {
        ReadableOrderConfirmation confirmation = orderService.checkout(code, order);
        return ResponseEntity.ok(confirmation);
    }

    @Override
    public ResponseEntity<ReadableOrderList> list(Integer count, Integer page) {
        ReadableOrderList orders = orderService.list(count, page);
        return ResponseEntity.ok(orders);
    }

    @Override
    public ResponseEntity<ReadableOrder> getOrder(Long id) {
        ReadableOrder order = orderService.getOrder(id);
        return ResponseEntity.ok(order);
    }

    @Override
    public ResponseEntity<ReadableOrderList> list(int count, String email, Long id, String name, int page, String phone, String status) {
        ReadableOrderList orders = orderService.list(count, email, id, name, page, phone, status);
        return ResponseEntity.ok(orders);
    }

    @Override
    public ResponseEntity<ReadableOrderList> list(Integer count, Long id, Integer start) {
        ReadableOrderList orders = orderService.listByCustomer(count, id, start);
        return ResponseEntity.ok(orders);
    }

    @Override
    public ResponseEntity<ReadableOrder> get(Long id) {
        ReadableOrder order = orderService.get(id);
        return ResponseEntity.ok(order);
    }

    @Override
    public ResponseEntity<Void> updateOrderCustomer(Long id, Customer orderCustomer) {
        orderService.updateOrderCustomer(id, orderCustomer);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateOrderStatus(Long id, String status) {
        orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok().build();
    }
}