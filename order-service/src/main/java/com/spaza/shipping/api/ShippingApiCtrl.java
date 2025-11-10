package com.spaza.shipping.api;

import com.spaza.shipping.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface ShippingApiCtrl {

    @GetMapping("/api/v1/auth/cart/{code}/shipping")
    ResponseEntity<ReadableShippingSummary> shipping(@PathVariable String code);

    @PostMapping("/api/v1/cart/{code}/shipping")
    ResponseEntity<ReadableShippingSummary> shipping(@PathVariable String code, @Valid @RequestBody AddressLocation address);

    @GetMapping("/api/v1/private/modules/shipping")
    ResponseEntity<Object[]> shippingModules();

    @GetMapping("/api/v1/private/modules/shipping/{code}")
    ResponseEntity<Object[]> shippingModule(@PathVariable String code);

    @PostMapping("/api/v1/private/modules/shipping")
    ResponseEntity<Void> configure(@Valid @RequestBody IntegrationModuleConfiguration configuration);
}