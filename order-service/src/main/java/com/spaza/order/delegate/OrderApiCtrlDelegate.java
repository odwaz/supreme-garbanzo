package com.spaza.order.delegate;

import com.spaza.order.model.*;
import org.springframework.http.ResponseEntity;

public interface OrderApiCtrlDelegate {
    ResponseEntity<ReadableOrderConfirmation> checkout(String code, PersistableOrder order);
    ResponseEntity<ReadableOrderConfirmation> checkout(String code, PersistableAnonymousOrder order);
    ResponseEntity<ReadableOrderList> list(Integer count, Integer page);
    ResponseEntity<ReadableOrder> getOrder(Long id);
    ResponseEntity<ReadableOrderList> list(int count, String email, Long id, String name, int page, String phone, String status, Long merchantId);
    ResponseEntity<ReadableOrderList> list(Integer count, Long id, Integer start);
    ResponseEntity<ReadableOrder> get(Long id);
    ResponseEntity<Void> updateOrderCustomer(Long id, Customer orderCustomer);
    ResponseEntity<Void> updateOrderStatus(Long id, String status);
}