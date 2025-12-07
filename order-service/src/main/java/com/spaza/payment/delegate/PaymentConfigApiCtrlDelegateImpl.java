package com.spaza.payment.delegate;

import com.spaza.payment.model.*;
import com.spaza.payment.service.PaymentConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentConfigApiCtrlDelegateImpl implements PaymentConfigApiCtrlDelegate {

    private final PaymentConfigService paymentConfigService;

    @Override
    public ResponseEntity<ReadablePaymentConfiguration> getPaymentConfiguration() {
        return ResponseEntity.ok(paymentConfigService.getConfiguration());
    }

    @Override
    public ResponseEntity<Void> createPaymentConfiguration(PersistablePaymentConfiguration configuration) {
        paymentConfigService.createConfiguration(configuration);
        return ResponseEntity.ok().build();
    }
}
