package com.spaza.shipping.service;

import com.spaza.shipping.model.*;
import com.spaza.shipping.repository.ShippingConfigRepository;
import org.springframework.stereotype.Service;

@Service
public class ShippingConfigService {

    private final ShippingConfigRepository repository;

    public ShippingConfigService(ShippingConfigRepository repository) {
        this.repository = repository;
    }

    public ReadableShippingConfiguration getConfiguration() {
        return repository.findAll().stream()
            .findFirst()
            .map(this::toReadable)
            .orElse(null);
    }

    public void createConfiguration(PersistableShippingConfiguration configuration) {
        ShippingConfig entity = new ShippingConfig();
        entity.setCode(configuration.getCode());
        entity.setName(configuration.getName());
        entity.setActive(configuration.getActive());
        repository.save(entity);
    }

    public void updateConfiguration(PersistableShippingConfiguration configuration) {
        repository.findByCode(configuration.getCode()).ifPresent(entity -> {
            entity.setName(configuration.getName());
            entity.setActive(configuration.getActive());
            repository.save(entity);
        });
    }

    public void deleteConfiguration(String code) {
        repository.findByCode(code).ifPresent(repository::delete);
    }

    private ReadableShippingConfiguration toReadable(ShippingConfig entity) {
        ReadableShippingConfiguration readable = new ReadableShippingConfiguration();
        readable.setCode(entity.getCode());
        readable.setName(entity.getName());
        readable.setActive(entity.getActive());
        return readable;
    }
}
