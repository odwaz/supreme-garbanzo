package com.spaza.shipping.delegate;

import com.spaza.shipping.model.*;
import org.springframework.http.ResponseEntity;

public interface ShippingApiCtrlDelegate {
    ResponseEntity<ReadableShippingSummary> shipping(String code);
    ResponseEntity<ReadableShippingSummary> shipping(String code, AddressLocation address);
    ResponseEntity<Object[]> shippingModules();
    ResponseEntity<Object[]> shippingModule(String code);
    ResponseEntity<Void> configure(IntegrationModuleConfiguration configuration);
}