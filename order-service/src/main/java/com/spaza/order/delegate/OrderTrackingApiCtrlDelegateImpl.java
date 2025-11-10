package com.spaza.order.delegate;

import com.spaza.order.model.*;
import com.spaza.order.service.OrderTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderTrackingApiCtrlDelegateImpl implements OrderTrackingApiCtrlDelegate {

    private final OrderTrackingService trackingService;

    @Override
    public ResponseEntity<ReadableOrderTracking> getTracking(Long id) {
        return ResponseEntity.ok(trackingService.getTracking(id));
    }

    @Override
    public ResponseEntity<ReadableOrderHistory[]> getHistory(Long id) {
        return ResponseEntity.ok(trackingService.getHistory(id));
    }
}
