package com.spaza.order.controller;

import com.spaza.order.api.OrderTrackingApiCtrl;
import com.spaza.order.delegate.OrderTrackingApiCtrlDelegate;
import com.spaza.order.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class OrderTrackingApiCtrlController implements OrderTrackingApiCtrl {

    @Autowired
    private OrderTrackingApiCtrlDelegate delegate;

    @Override
    public ResponseEntity<ReadableOrderTracking> getTracking(Long id) {
        return delegate.getTracking(id);
    }

    @Override
    public ResponseEntity<ReadableOrderHistory[]> getHistory(Long id) {
        return delegate.getHistory(id);
    }
}
