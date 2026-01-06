package com.spaza.shipping.delegate;

import com.spaza.shipping.model.*;
import org.springframework.http.ResponseEntity;

public interface ShippingConfigApiCtrlDelegate {
    ResponseEntity<ReadableShippingConfiguration> getShippingConfiguration();
    ResponseEntity<Void> createShippingConfiguration(PersistableShippingConfiguration configuration);
}
