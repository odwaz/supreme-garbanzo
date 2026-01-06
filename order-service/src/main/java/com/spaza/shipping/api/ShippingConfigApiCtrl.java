package com.spaza.shipping.api;

import com.spaza.shipping.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface ShippingConfigApiCtrl {

    @GetMapping("/api/v1/private/configurations/shipping")
    ResponseEntity<ReadableShippingConfiguration> getShippingConfiguration();

    @PostMapping("/api/v1/private/configurations/shipping")
    ResponseEntity<Void> createShippingConfiguration(@Valid @RequestBody PersistableShippingConfiguration configuration);
}
