package com.spaza.payment.controller;

import com.spaza.payment.api.PaymentConfigApiCtrl;
import com.spaza.payment.delegate.PaymentConfigApiCtrlDelegate;
import com.spaza.payment.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

public class PaymentConfigApiCtrlController implements PaymentConfigApiCtrl {

    @Autowired
    private PaymentConfigApiCtrlDelegate delegate;

    @Override
    public ResponseEntity<ReadablePaymentConfiguration> getPaymentConfiguration() {
        return delegate.getPaymentConfiguration();
    }

    @Override
    public ResponseEntity<Void> createPaymentConfiguration(PersistablePaymentConfiguration configuration) {
        return delegate.createPaymentConfiguration(configuration);
    }
}
