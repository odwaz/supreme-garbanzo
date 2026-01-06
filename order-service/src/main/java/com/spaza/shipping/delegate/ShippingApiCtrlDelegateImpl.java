package com.spaza.shipping.delegate;

import com.spaza.shipping.model.*;
import com.spaza.shipping.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ShippingApiCtrlDelegateImpl implements ShippingApiCtrlDelegate {

    @Autowired
    private ShippingService shippingService;

    @Override
    public ResponseEntity<ReadableShippingSummary> shipping(String code) {
        ReadableShippingSummary summary = shippingService.getShipping(code);
        return ResponseEntity.ok(summary);
    }

    @Override
    public ResponseEntity<ReadableShippingSummary> shipping(String code, AddressLocation address) {
        ReadableShippingSummary summary = shippingService.calculateShipping(code, address);
        return ResponseEntity.ok(summary);
    }

    @Override
    public ResponseEntity<Object[]> shippingModules() {
        Object[] modules = shippingService.getShippingModules();
        return ResponseEntity.ok(modules);
    }

    @Override
    public ResponseEntity<Object[]> shippingModule(String code) {
        Object[] module = shippingService.getShippingModule(code);
        return ResponseEntity.ok(module);
    }

    @Override
    public ResponseEntity<Void> configure(IntegrationModuleConfiguration configuration) {
        shippingService.configure(configuration);
        return ResponseEntity.ok().build();
    }
}