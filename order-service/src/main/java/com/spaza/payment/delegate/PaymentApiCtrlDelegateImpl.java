package com.spaza.payment.delegate;

import com.spaza.order.exception.*;
import com.spaza.payment.model.*;
import com.spaza.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@Slf4j
public class PaymentApiCtrlDelegateImpl implements PaymentApiCtrlDelegate {

    @Autowired
    private PaymentService paymentService;

    @Override
    public ResponseEntity<ReadableTransaction> initAuth(String code, PersistablePayment payment) {
        try {
            log.info("Initializing auth payment with code={}", code);
            ReadableTransaction transaction = paymentService.init(code, payment);
            log.info("Auth payment initialized successfully");
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            log.error("Failed to initialize auth payment with code={}", code, e);
            throw new PaymentFailedException("Failed to initialize payment authorization");
        }
    }

    @Override
    public ResponseEntity<ReadableTransaction> init(String code, PersistablePayment payment) {
        try {
            log.info("Initializing payment with code={}", code);
            ReadableTransaction transaction = paymentService.init(code, payment);
            log.info("Payment initialized successfully");
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            log.error("Failed to initialize payment with code={}", code, e);
            throw new PaymentFailedException("Failed to initialize payment");
        }
    }

    @Override
    public ResponseEntity<ReadableTransaction> authorizePayment(Long id) {
        try {
            log.info("Authorizing payment: transactionId={}", id);
            ReadableTransaction transaction = paymentService.authorize(id);
            log.info("Payment authorized successfully: transactionId={}", id);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            log.error("Failed to authorize payment: transactionId={}", id, e);
            throw new PaymentFailedException("Transaction " + id, null, e);
        }
    }

    @Override
    public ResponseEntity<ReadableTransaction> capturePayment(Long id) {
        try {
            log.info("Capturing payment: transactionId={}", id);
            ReadableTransaction transaction = paymentService.capture(id);
            log.info("Payment captured successfully: transactionId={}", id);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            log.error("Failed to capture payment: transactionId={}", id, e);
            throw new PaymentFailedException("Transaction " + id, null, e);
        }
    }

    @Override
    public ResponseEntity<ReadableTransaction> refundPayment(Long id) {
        try {
            log.info("Refunding payment: transactionId={}", id);
            ReadableTransaction transaction = paymentService.refund(id);
            log.info("Payment refunded successfully: transactionId={}", id);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            log.error("Failed to refund payment: transactionId={}", id, e);
            throw new PaymentFailedException("Transaction " + id, null, e);
        }
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
    public ResponseEntity<Object[]> paymentModules(Long merchantId, String language) {
        Object[] modules = paymentService.getPaymentModules(merchantId);
        return ResponseEntity.ok(modules);
    }

    @Override
    public ResponseEntity<Object[]> paymentModule(String code, Long merchantId) {
        Object[] module = paymentService.getPaymentModule(code, merchantId);
        return ResponseEntity.ok(module);
    }

    @Override
    public ResponseEntity<Void> configure(IntegrationModuleConfiguration configuration) {
        paymentService.configure(configuration);
        return ResponseEntity.ok().build();
    }
}