package com.spaza.payment.delegate;

import com.spaza.payment.model.*;
import org.springframework.http.ResponseEntity;

public interface PaymentConfigApiCtrlDelegate {
    ResponseEntity<ReadablePaymentConfiguration> getPaymentConfiguration();
    ResponseEntity<Void> createPaymentConfiguration(PersistablePaymentConfiguration configuration);
    ResponseEntity<Void> updatePaymentConfiguration(PersistablePaymentConfiguration configuration);
    ResponseEntity<Void> deletePaymentConfiguration(String code);
}
