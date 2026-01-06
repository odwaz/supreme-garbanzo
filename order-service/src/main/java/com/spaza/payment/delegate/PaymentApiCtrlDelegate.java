package com.spaza.payment.delegate;

import com.spaza.payment.model.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public interface PaymentApiCtrlDelegate {
    ResponseEntity<ReadableTransaction> initAuth(String code, PersistablePayment payment);
    ResponseEntity<ReadableTransaction> init(String code, PersistablePayment payment);
    ResponseEntity<ReadableTransaction> authorizePayment(Long id);
    ResponseEntity<ReadableTransaction> capturePayment(Long id);
    ResponseEntity<ReadableTransaction> refundPayment(Long id);
    ResponseEntity<ReadableTransaction[]> listTransactions(Long id);
    ResponseEntity<String> nextTransaction(Long id);
    ResponseEntity<Object[]> paymentModules(Long merchantId, String language);
    ResponseEntity<Object[]> paymentModule(String code, Long merchantId);
    ResponseEntity<Void> configure(IntegrationModuleConfiguration configuration);
}