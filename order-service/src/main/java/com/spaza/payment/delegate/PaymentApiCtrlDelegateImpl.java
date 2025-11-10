package com.spaza.payment.delegate;

import com.spaza.payment.model.*;
import com.spaza.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentApiCtrlDelegateImpl implements PaymentApiCtrlDelegate {

    @Autowired
    private PaymentService paymentService;

    @Override
    public ResponseEntity<ReadableTransaction> initAuth(String code, PersistablePayment payment) {
        ReadableTransaction transaction = paymentService.init(code, payment);
        return ResponseEntity.ok(transaction);
    }

    @Override
    public ResponseEntity<ReadableTransaction> init(String code, PersistablePayment payment) {
        ReadableTransaction transaction = paymentService.init(code, payment);
        return ResponseEntity.ok(transaction);
    }

    @Override
    public ResponseEntity<ReadableTransaction> authorizePayment(Long id) {
        ReadableTransaction transaction = paymentService.authorize(id);
        return ResponseEntity.ok(transaction);
    }

    @Override
    public ResponseEntity<ReadableTransaction> capturePayment(Long id) {
        ReadableTransaction transaction = paymentService.capture(id);
        return ResponseEntity.ok(transaction);
    }

    @Override
    public ResponseEntity<ReadableTransaction> refundPayment(Long id) {
        ReadableTransaction transaction = paymentService.refund(id);
        return ResponseEntity.ok(transaction);
    }

    @Override
    public ResponseEntity<ReadableTransaction[]> listTransactions(Long id) {
        ReadableTransaction[] transactions = paymentService.listTransactions(id);
        return ResponseEntity.ok(transactions);
    }

    @Override
    public ResponseEntity<String> nextTransaction(Long id) {
        String next = paymentService.nextTransaction(id);
        return ResponseEntity.ok(next);
    }

    @Override
    public ResponseEntity<Object[]> paymentModules() {
        Object[] modules = paymentService.getPaymentModules();
        return ResponseEntity.ok(modules);
    }

    @Override
    public ResponseEntity<Object[]> paymentModule(String code) {
        Object[] module = paymentService.getPaymentModule(code);
        return ResponseEntity.ok(module);
    }

    @Override
    public ResponseEntity<Void> configure(IntegrationModuleConfiguration configuration) {
        paymentService.configure(configuration);
        return ResponseEntity.ok().build();
    }
}