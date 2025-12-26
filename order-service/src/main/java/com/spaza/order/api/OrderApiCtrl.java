package com.spaza.order.api;

import com.spaza.order.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface OrderApiCtrl {

    @PostMapping("/api/v1/auth/cart/{code}/checkout")
    ResponseEntity<ReadableOrderConfirmation> checkout(@PathVariable("code") String code, @Valid @RequestBody PersistableOrder order);

    @PostMapping("/api/v1/cart/{code}/checkout")
    ResponseEntity<ReadableOrderConfirmation> checkout(@PathVariable("code") String code, @Valid @RequestBody PersistableAnonymousOrder order);

    @GetMapping("/api/v1/auth/orders")
    ResponseEntity<ReadableOrderList> list(@RequestParam(required = false) Integer count, @RequestParam(required = false) Integer page);

    @GetMapping("/api/v1/auth/orders/{id}")
    ResponseEntity<ReadableOrder> getOrder(@PathVariable("id") Long id);

    @GetMapping("/api/v1/private/orders")
    ResponseEntity<ReadableOrderList> list(@RequestParam(defaultValue = "25") int count, @RequestParam(required = false) String email,
                                          @RequestParam(required = false) Long id, @RequestParam(required = false) String name,
                                          @RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String phone,
                                          @RequestParam(required = false) String status, @RequestParam(required = false) Long merchantId);

    @GetMapping("/api/v1/private/orders/customers/{id}")
    ResponseEntity<ReadableOrderList> list(@RequestParam(required = false) Integer count, @PathVariable("id") Long id, @RequestParam(required = false) Integer start);

    @GetMapping("/api/v1/private/orders/{id}")
    ResponseEntity<ReadableOrder> get(@PathVariable("id") Long id);

    @PatchMapping("/api/v1/private/orders/{id}/customer")
    ResponseEntity<Void> updateOrderCustomer(@PathVariable("id") Long id, @Valid @RequestBody Customer orderCustomer);

    @PutMapping("/api/v1/private/orders/{id}/status")
    ResponseEntity<Void> updateOrderStatus(@PathVariable("id") Long id, @Valid @RequestBody String status);
}