package com.spaza.shipping.delegate;

import com.spaza.shipping.model.*;
import com.spaza.shipping.service.ShippingConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShippingConfigApiCtrlDelegateImpl implements ShippingConfigApiCtrlDelegate {

    private final ShippingConfigService shippingConfigService;

    @Override
    public ResponseEntity<ReadableShippingConfiguration> getShippingConfiguration() {
        return ResponseEntity.ok(shippingConfigService.getConfiguration());
    }

    @Override
    public ResponseEntity<Void> createShippingConfiguration(PersistableShippingConfiguration configuration) {
        shippingConfigService.createConfiguration(configuration);
        return ResponseEntity.ok().build();
    }
}
