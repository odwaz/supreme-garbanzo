package com.spaza.order.delegate;

import com.spaza.order.model.*;
import com.spaza.order.service.OrderService;
import com.spaza.order.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
public class OrderApiCtrlDelegateImpl implements OrderApiCtrlDelegate {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    public OrderApiCtrlDelegateImpl(OrderService orderService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseEntity<ReadableOrderConfirmation> checkout(String code, PersistableOrder order) {
        String email = extractEmailFromToken();
        Long customerId = orderService.getCustomerIdByEmail(email);
        ReadableOrderConfirmation confirmation = orderService.createOrder(code, order, customerId);
        return ResponseEntity.ok(confirmation);
    }
    
    private String extractEmailFromToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractEmail(token);
        }
        return null;
    }

    @Override
    public ResponseEntity<ReadableOrderConfirmation> checkout(String code, PersistableAnonymousOrder order) {
        ReadableOrderConfirmation confirmation = orderService.createAnonymousOrder(code, order);
        return ResponseEntity.ok(confirmation);
    }

    @Override
    public ResponseEntity<ReadableOrderList> list(Integer count, Integer page) {
        String email = extractEmailFromToken();
        Long customerId = orderService.getCustomerIdByEmail(email);
        ReadableOrderList orders = orderService.getCustomerOrders(customerId);
        return ResponseEntity.ok(orders);
    }

    @Override
    public ResponseEntity<ReadableOrder> getOrder(Long id) {
        ReadableOrder order = orderService.getOrder(id);
        return ResponseEntity.ok(order);
    }

    @Override
    public ResponseEntity<ReadableOrderList> list(int count, String email, Long id, String name, int page, String phone, String status, Long merchantId) {
        ReadableOrderList orders = orderService.searchOrders(status, merchantId);
        return ResponseEntity.ok(orders);
    }

    @Override
    public ResponseEntity<ReadableOrderList> list(Integer count, Long id, Integer start) {
        ReadableOrderList orders = orderService.getCustomerOrders(id);
        return ResponseEntity.ok(orders);
    }

    @Override
    public ResponseEntity<ReadableOrder> get(Long id) {
        return getOrder(id);
    }

    @Override
    public ResponseEntity<Void> updateOrderCustomer(Long id, Customer orderCustomer) {
        orderService.updateCustomer(id, orderCustomer);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateOrderStatus(Long id, String status) {
        orderService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
