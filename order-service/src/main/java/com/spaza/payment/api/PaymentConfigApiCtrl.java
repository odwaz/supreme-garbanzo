package com.spaza.payment.api;

import com.spaza.payment.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface PaymentConfigApiCtrl {

    @GetMapping("/api/v1/private/configurations/payment")
    ResponseEntity<ReadablePaymentConfiguration> getPaymentConfiguration();

    @PostMapping("/api/v1/private/configurations/payment")
    ResponseEntity<Void> createPaymentConfiguration(@Valid @RequestBody PersistablePaymentConfiguration configuration);

    @PutMapping("/api/v1/private/configurations/payment")
    ResponseEntity<Void> updatePaymentConfiguration(@Valid @RequestBody PersistablePaymentConfiguration configuration);

    @DeleteMapping("/api/v1/private/configurations/payment/{code}")
    ResponseEntity<Void> deletePaymentConfiguration(@PathVariable String code);
}
