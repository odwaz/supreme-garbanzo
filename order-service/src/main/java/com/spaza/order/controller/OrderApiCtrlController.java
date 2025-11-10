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
    public ResponseEntity<ReadableOrderConfirmation> checkout(@PathVariable String code, @Valid @RequestBody PersistableOrder order) {
        return delegate.checkout(code, order);
    }

    @Override
    public ResponseEntity<ReadableOrderConfirmation> checkout(@PathVariable String code, @Valid @RequestBody PersistableAnonymousOrder order) {
        return delegate.checkout(code, order);
    }

    @Override
    public ResponseEntity<ReadableOrderList> list(@RequestParam(required = false) Integer count, @RequestParam(required = false) Integer page) {
        return delegate.list(count, page);
    }

    @Override
    public ResponseEntity<ReadableOrder> getOrder(@PathVariable Long id) {
        return delegate.getOrder(id);
    }

    @Override
    public ResponseEntity<ReadableOrderList> list(@RequestParam(defaultValue = "25") int count, @RequestParam(required = false) String email, @RequestParam(required = false) Long id, @RequestParam(required = false) String name, @RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String phone, @RequestParam(required = false) String status) {
        return delegate.list(count, email, id, name, page, phone, status);
    }

    @Override
    public ResponseEntity<ReadableOrderList> list(@RequestParam(required = false) Integer count, @PathVariable Long id, @RequestParam(required = false) Integer start) {
        return delegate.list(count, id, start);
    }

    @Override
    public ResponseEntity<ReadableOrder> get(@PathVariable Long id) {
        return delegate.get(id);
    }

    @Override
    public ResponseEntity<Void> updateOrderCustomer(@PathVariable Long id, @Valid @RequestBody Customer orderCustomer) {
        return delegate.updateOrderCustomer(id, orderCustomer);
    }

    @Override
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody String status) {
        return delegate.updateOrderStatus(id, status);
    }
}