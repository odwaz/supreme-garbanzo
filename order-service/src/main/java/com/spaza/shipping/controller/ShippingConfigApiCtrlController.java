package com.spaza.shipping.controller;

import com.spaza.shipping.api.ShippingConfigApiCtrl;
import com.spaza.shipping.delegate.ShippingConfigApiCtrlDelegate;
import com.spaza.shipping.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

public class ShippingConfigApiCtrlController implements ShippingConfigApiCtrl {

    @Autowired
    private ShippingConfigApiCtrlDelegate delegate;

    @Override
    public ResponseEntity<ReadableShippingConfiguration> getShippingConfiguration() {
        return delegate.getShippingConfiguration();
    }

    @Override
    public ResponseEntity<Void> createShippingConfiguration(PersistableShippingConfiguration configuration) {
        return delegate.createShippingConfiguration(configuration);
    }
}
