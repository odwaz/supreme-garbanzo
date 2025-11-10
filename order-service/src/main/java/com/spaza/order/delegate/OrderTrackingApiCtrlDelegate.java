package com.spaza.order.delegate;

import com.spaza.order.model.*;
import org.springframework.http.ResponseEntity;

public interface OrderTrackingApiCtrlDelegate {
    ResponseEntity<ReadableOrderTracking> getTracking(Long id);
    ResponseEntity<ReadableOrderHistory[]> getHistory(Long id);
}
