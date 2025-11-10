package com.spaza.order.api;

import com.spaza.order.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface OrderTrackingApiCtrl {

    @GetMapping("/api/v1/auth/orders/{id}/tracking")
    ResponseEntity<ReadableOrderTracking> getTracking(@PathVariable Long id);

    @GetMapping("/api/v1/auth/orders/{id}/history")
    ResponseEntity<ReadableOrderHistory[]> getHistory(@PathVariable Long id);
}
