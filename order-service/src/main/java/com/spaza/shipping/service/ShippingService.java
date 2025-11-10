package com.spaza.shipping.service;

import com.spaza.shipping.model.*;
import com.spaza.shipping.repository.ShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShippingService {

    @Autowired
    private ShippingRepository shippingRepository;

    public ReadableShippingSummary getShipping(String cartCode) {
        ReadableShippingSummary summary = new ReadableShippingSummary();
        summary.setCartCode(cartCode);
        summary.setShippingOptions(createShippingOptions());
        return summary;
    }

    public ReadableShippingSummary calculateShipping(String cartCode, AddressLocation address) {
        ReadableShippingSummary summary = new ReadableShippingSummary();
        summary.setCartCode(cartCode);
        summary.setShippingOptions(createShippingOptions());
        summary.setDeliveryAddress(address);
        return summary;
    }

    public Object[] getShippingModules() {
        return new Object[]{"STANDARD", "EXPRESS", "OVERNIGHT"};
    }

    public Object[] getShippingModule(String code) {
        return new Object[]{Map.of("code", code, "name", code + " Shipping", "enabled", true)};
    }

    public void configure(IntegrationModuleConfiguration configuration) {
        // Configuration logic
    }

    private List<ShippingOption> createShippingOptions() {
        List<ShippingOption> options = new ArrayList<>();
        
        ShippingOption standard = new ShippingOption();
        standard.setCode("STANDARD");
        standard.setName("Standard Shipping");
        standard.setCost(5.99);
        standard.setEstimatedDays(5);
        options.add(standard);
        
        ShippingOption express = new ShippingOption();
        express.setCode("EXPRESS");
        express.setName("Express Shipping");
        express.setCost(12.99);
        express.setEstimatedDays(2);
        options.add(express);
        
        return options;
    }
}