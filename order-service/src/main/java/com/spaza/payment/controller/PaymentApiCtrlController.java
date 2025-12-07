package com.spaza.payment.controller;

import com.spaza.payment.api.PaymentApiCtrl;
import com.spaza.payment.delegate.PaymentApiCtrlDelegate;
import com.spaza.payment.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class PaymentApiCtrlController implements PaymentApiCtrl {

    @Autowired
    private PaymentApiCtrlDelegate delegate;

    @Override
    public ResponseEntity<ReadableTransaction> initAuth(@PathVariable String code, @Valid @RequestBody PersistablePayment payment) {
        return delegate.initAuth(code, payment);
    }

    @Override
    public ResponseEntity<ReadableTransaction> init(@PathVariable String code, @Valid @RequestBody PersistablePayment payment) {
        return delegate.init(code, payment);
    }

    @Override
    public ResponseEntity<ReadableTransaction> authorizePayment(@PathVariable Long id) {
        return delegate.authorizePayment(id);
    }

    @Override
    public ResponseEntity<ReadableTransaction> capturePayment(@PathVariable Long id) {
        return delegate.capturePayment(id);
    }

    @Override
    public ResponseEntity<ReadableTransaction> refundPayment(@PathVariable Long id) {
        return delegate.refundPayment(id);
    }

    @Override
    public ResponseEntity<ReadableTransaction[]> listTransactions(@PathVariable Long id) {
        return delegate.listTransactions(id);
    }

    @Override
    public ResponseEntity<String> nextTransaction(@PathVariable Long id) {
        return delegate.nextTransaction(id);
    }

    @Override
    public ResponseEntity<Object[]> paymentModules(@RequestParam(required = false) Long merchantId, @RequestParam(required = false) String language) {
        return delegate.paymentModules(merchantId, language);
    }

    @Override
    public ResponseEntity<Object[]> paymentModule(@PathVariable String code, @RequestParam(required = false) Long merchantId) {
        return delegate.paymentModule(code, merchantId);
    }

    @Override
    public ResponseEntity<Void> configure(@Valid @RequestBody IntegrationModuleConfiguration configuration) {
        return delegate.configure(configuration);
    }
}