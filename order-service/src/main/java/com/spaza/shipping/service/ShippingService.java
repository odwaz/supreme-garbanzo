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
        summary.setShippingOptions(getDefaultShippingOptions());
        return summary;
    }

    public ReadableShippingSummary calculateShipping(String cartCode, AddressLocation address) {
        ReadableShippingSummary summary = new ReadableShippingSummary();
        summary.setCartCode(cartCode);
        summary.setDeliveryAddress(address);
        summary.setShippingOptions(calculateShippingOptions(address));
        return summary;
    }

    public Object[] getShippingModules() {
        return new Object[]{
            Map.of("code", "PICKUP", "name", "Store Pickup", "enabled", true),
            Map.of("code", "DELIVERY", "name", "Home Delivery", "enabled", true),
            Map.of("code", "COURIER", "name", "Courier Service", "enabled", true)
        };
    }

    public Object[] getShippingModule(String code) {
        Map<String, Object> module = new HashMap<>();
        module.put("code", code);
        module.put("enabled", true);
        
        switch (code) {
            case "PICKUP":
                module.put("name", "Store Pickup");
                module.put("description", "Pick up from store");
                module.put("cost", 0.0);
                break;
            case "DELIVERY":
                module.put("name", "Home Delivery");
                module.put("description", "Delivery to your address");
                module.put("cost", 50.0);
                break;
            case "COURIER":
                module.put("name", "Courier Service");
                module.put("description", "Fast courier delivery");
                module.put("cost", 120.0);
                break;
        }
        
        return new Object[]{module};
    }

    public void configure(IntegrationModuleConfiguration configuration) {
        System.out.println("Configuring shipping module: " + configuration.getCode());
    }

    private List<ShippingOption> getDefaultShippingOptions() {
        List<ShippingOption> options = new ArrayList<>();
        
        ShippingOption pickup = new ShippingOption();
        pickup.setCode("PICKUP");
        pickup.setName("Store Pickup");
        pickup.setCost(0.0);
        pickup.setEstimatedDays(0);
        options.add(pickup);
        
        ShippingOption delivery = new ShippingOption();
        delivery.setCode("DELIVERY");
        delivery.setName("Home Delivery");
        delivery.setCost(50.0);
        delivery.setEstimatedDays(3);
        options.add(delivery);
        
        ShippingOption courier = new ShippingOption();
        courier.setCode("COURIER");
        courier.setName("Courier Service");
        courier.setCost(120.0);
        courier.setEstimatedDays(1);
        options.add(courier);
        
        return options;
    }

    private List<ShippingOption> calculateShippingOptions(AddressLocation address) {
        List<ShippingOption> options = getDefaultShippingOptions();
        
        if (address.getCity() != null && !address.getCity().isEmpty()) {
            for (ShippingOption option : options) {
                if ("DELIVERY".equals(option.getCode())) {
                    option.setCost(option.getCost() + 20.0);
                }
            }
        }
        
        return options;
    }

    public Object[] listShipping(String searchJoin, String search) {
        return new Object[]{
            Map.of("id", 1, "code", "PICKUP", "name", "Store Pickup", "enabled", true, "cost", 0.0),
            Map.of("id", 2, "code", "DELIVERY", "name", "Home Delivery", "enabled", true, "cost", 50.0),
            Map.of("id", 3, "code", "COURIER", "name", "Courier Service", "enabled", true, "cost", 120.0)
        };
    }
}