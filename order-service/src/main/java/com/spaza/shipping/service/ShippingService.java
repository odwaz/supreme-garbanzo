package com.spaza.shipping.service;

import com.spaza.shipping.model.*;
import com.spaza.shipping.repository.ShippingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShippingService {

    private static final Logger log = LoggerFactory.getLogger(ShippingService.class);
    private static final String ENABLED = "enabled";
    private static final String PICKUP = "PICKUP";
    private static final String STORE_PICKUP = "Store Pickup";
    private static final String DELIVERY = "DELIVERY";
    private static final String HOME_DELIVERY = "Home Delivery";
    private static final String COURIER = "COURIER";
    private static final String COURIER_SERVICE = "Courier Service";
    private static final String DESCRIPTION = "description";

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
            Map.of("code", PICKUP, "name", STORE_PICKUP, ENABLED, true),
            Map.of("code", DELIVERY, "name", HOME_DELIVERY, ENABLED, true),
            Map.of("code", COURIER, "name", COURIER_SERVICE, ENABLED, true)
        };
    }

    public Object[] getShippingModule(String code) {
        Map<String, Object> module = new HashMap<>();
        module.put("code", code);
        module.put(ENABLED, true);
        
        switch (code) {
            case PICKUP:
                module.put("name", STORE_PICKUP);
                module.put(DESCRIPTION, "Pick up from store");
                module.put("cost", 0.0);
                break;
            case DELIVERY:
                module.put("name", HOME_DELIVERY);
                module.put(DESCRIPTION, "Delivery to your address");
                module.put("cost", 50.0);
                break;
            case COURIER:
                module.put("name", COURIER_SERVICE);
                module.put(DESCRIPTION, "Fast courier delivery");
                module.put("cost", 120.0);
                break;
            default:
                break;
        }
        
        return new Object[]{module};
    }

    public void configure(IntegrationModuleConfiguration configuration) {
        log.info("Configuring shipping module: {}", configuration.getCode());
    }

    private List<ShippingOption> getDefaultShippingOptions() {
        List<ShippingOption> options = new ArrayList<>();
        
        ShippingOption pickup = new ShippingOption();
        pickup.setCode(PICKUP);
        pickup.setName(STORE_PICKUP);
        pickup.setCost(0.0);
        pickup.setEstimatedDays(0);
        options.add(pickup);
        
        ShippingOption delivery = new ShippingOption();
        delivery.setCode(DELIVERY);
        delivery.setName(HOME_DELIVERY);
        delivery.setCost(50.0);
        delivery.setEstimatedDays(3);
        options.add(delivery);
        
        ShippingOption courier = new ShippingOption();
        courier.setCode(COURIER);
        courier.setName(COURIER_SERVICE);
        courier.setCost(120.0);
        courier.setEstimatedDays(1);
        options.add(courier);
        
        return options;
    }

    private List<ShippingOption> calculateShippingOptions(AddressLocation address) {
        List<ShippingOption> options = getDefaultShippingOptions();
        
        if (address.getCity() != null && !address.getCity().isEmpty()) {
            for (ShippingOption option : options) {
                if (DELIVERY.equals(option.getCode())) {
                    option.setCost(option.getCost() + 20.0);
                }
            }
        }
        
        return options;
    }

    public Object[] listShipping() {
        return new Object[]{
            Map.of("id", 1, "code", PICKUP, "name", STORE_PICKUP, ENABLED, true, "cost", 0.0),
            Map.of("id", 2, "code", DELIVERY, "name", HOME_DELIVERY, ENABLED, true, "cost", 50.0),
            Map.of("id", 3, "code", COURIER, "name", COURIER_SERVICE, ENABLED, true, "cost", 120.0)
        };
    }
}