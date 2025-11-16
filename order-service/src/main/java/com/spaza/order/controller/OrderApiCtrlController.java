package com.spaza.order.controller;

import com.spaza.order.api.OrderApiCtrl;
import com.spaza.order.delegate.OrderApiCtrlDelegate;
import com.spaza.order.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class OrderApiCtrlController implements OrderApiCtrl {
    
    @Autowired
    private OrderApiCtrlDelegate delegate;

    @Override
    public ResponseEntity<ReadableOrderConfirmation> checkout(String code, @Valid PersistableOrder order) {
        return delegate.checkout(code, order);
    }

    @Override
    public ResponseEntity<ReadableOrderConfirmation> checkout(String code, @Valid PersistableAnonymousOrder order) {
        return delegate.checkout(code, order);
    }

    @Override
    public ResponseEntity<ReadableOrderList> list(Integer count, Integer page) {
        return delegate.list(count, page);
    }

    @Override
    public ResponseEntity<ReadableOrder> getOrder(Long id) {
        return delegate.getOrder(id);
    }

    @Override
    public ResponseEntity<ReadableOrderList> list(int count, String email, Long id, String name, int page, String phone, String status) {
        return delegate.list(count, email, id, name, page, phone, status);
    }

    @Override
    public ResponseEntity<ReadableOrderList> list(Integer count, Long id, Integer start) {
        return delegate.list(count, id, start);
    }

    @Override
    public ResponseEntity<ReadableOrder> get(Long id) {
        return delegate.get(id);
    }

    @Override
    public ResponseEntity<Void> updateOrderCustomer(Long id, @Valid Customer orderCustomer) {
        return delegate.updateOrderCustomer(id, orderCustomer);
    }

    @Override
    public ResponseEntity<Void> updateOrderStatus(Long id, @Valid String status) {
        return delegate.updateOrderStatus(id, status);
    }
}
