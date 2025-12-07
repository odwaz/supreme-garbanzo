package com.spaza.shipping.controller;

import com.spaza.shipping.api.ShippingApiCtrl;
import com.spaza.shipping.delegate.ShippingApiCtrlDelegate;
import com.spaza.shipping.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController

public class ShippingApiCtrlController implements ShippingApiCtrl {

    @Autowired
    private ShippingApiCtrlDelegate delegate;

    @Override
    public ResponseEntity<ReadableShippingSummary> shipping(@PathVariable String code) {
        return delegate.shipping(code);
    }

    @Override
    public ResponseEntity<ReadableShippingSummary> shipping(@PathVariable String code, @Valid @RequestBody AddressLocation address) {
        return delegate.shipping(code, address);
    }

    @Override
    public ResponseEntity<Object[]> shippingModules() {
        return delegate.shippingModules();
    }

    @Override
    public ResponseEntity<Object[]> shippingModule(@PathVariable String code) {
        return delegate.shippingModule(code);
    }

    @Override
    public ResponseEntity<Void> configure(@Valid @RequestBody IntegrationModuleConfiguration configuration) {
        return delegate.configure(configuration);
    }
}
