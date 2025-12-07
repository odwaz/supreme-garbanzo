package com.spaza.payment.controller;

import com.spaza.payment.model.*;
import com.spaza.payment.service.MerchantPaymentConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/merchant")
public class MerchantPaymentConfigController {
    
    @Autowired
    private MerchantPaymentConfigService service;
    
    @PostMapping("/{merchantId}/payment/config")
    public ResponseEntity<ReadableMerchantPaymentConfig> saveConfig(
            @PathVariable Long merchantId,
            @RequestBody PersistableMerchantPaymentConfig config) {
        config.setMerchantId(merchantId);
        return ResponseEntity.ok(service.saveConfig(config));
    }
    
    @GetMapping("/{merchantId}/payment/config")
    public ResponseEntity<List<ReadableMerchantPaymentConfig>> getConfigs(@PathVariable Long merchantId) {
        return ResponseEntity.ok(service.getConfigs(merchantId));
    }
    
    @GetMapping("/{merchantId}/payment/config/{paymentMethod}")
    public ResponseEntity<ReadableMerchantPaymentConfig> getConfig(
            @PathVariable Long merchantId,
            @PathVariable String paymentMethod) {
        ReadableMerchantPaymentConfig config = service.getConfig(merchantId, paymentMethod);
        return config != null ? ResponseEntity.ok(config) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{merchantId}/payment/config/{paymentMethod}")
    public ResponseEntity<Void> deleteConfig(
            @PathVariable Long merchantId,
            @PathVariable String paymentMethod) {
        service.deleteConfig(merchantId, paymentMethod);
        return ResponseEntity.noContent().build();
    }
}
