package com.spaza.payment.api;

import com.spaza.payment.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface PaymentApiCtrl {

    @PostMapping("/api/v1/auth/cart/{code}/payment/init")
    ResponseEntity<ReadableTransaction> initAuth(@PathVariable String code, @Valid @RequestBody PersistablePayment payment);

    @PostMapping("/api/v1/cart/{code}/payment/init")
    ResponseEntity<ReadableTransaction> init(@PathVariable String code, @Valid @RequestBody PersistablePayment payment);

    @PostMapping("/api/v1/private/orders/{id}/authorize")
    ResponseEntity<ReadableTransaction> authorizePayment(@PathVariable Long id);

    @PostMapping("/api/v1/private/orders/{id}/capture")
    ResponseEntity<ReadableTransaction> capturePayment(@PathVariable Long id);

    @PostMapping("/api/v1/private/orders/{id}/refund")
    ResponseEntity<ReadableTransaction> refundPayment(@PathVariable Long id);

    @GetMapping("/api/v1/private/orders/{id}/payment/transactions")
    ResponseEntity<ReadableTransaction[]> listTransactions(@PathVariable Long id);

    @GetMapping("/api/v1/private/orders/{id}/payment/nextTransaction")
    ResponseEntity<String> nextTransaction(@PathVariable Long id);

    @GetMapping("/api/v1/private/modules/payment")
    ResponseEntity<Object[]> paymentModules();

    @GetMapping("/api/v1/private/modules/payment/{code}")
    ResponseEntity<Object[]> paymentModule(@PathVariable String code);

    @PostMapping("/api/v1/private/modules/payment")
    ResponseEntity<Void> configure(@Valid @RequestBody IntegrationModuleConfiguration configuration);
}