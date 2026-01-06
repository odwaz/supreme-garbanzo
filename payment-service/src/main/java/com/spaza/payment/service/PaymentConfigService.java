package com.spaza.payment.service;

import com.spaza.payment.model.*;
import com.spaza.payment.repository.PaymentConfigRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentConfigService {

    private final PaymentConfigRepository repository;

    public PaymentConfigService(PaymentConfigRepository repository) {
        this.repository = repository;
    }

    public ReadablePaymentConfiguration getConfiguration() {
        return repository.findAll().stream()
            .findFirst()
            .map(this::toReadable)
            .orElse(null);
    }

    public void createConfiguration(PersistablePaymentConfiguration configuration) {
        PaymentConfig entity = new PaymentConfig();
        entity.setCode(configuration.getCode());
        entity.setName(configuration.getName());
        entity.setActive(configuration.getActive());
        repository.save(entity);
    }

    public void updateConfiguration(PersistablePaymentConfiguration configuration) {
        repository.findByCode(configuration.getCode()).ifPresent(entity -> {
            entity.setName(configuration.getName());
            entity.setActive(configuration.getActive());
            repository.save(entity);
        });
    }

    public void deleteConfiguration(String code) {
        repository.findByCode(code).ifPresent(repository::delete);
    }

    private ReadablePaymentConfiguration toReadable(PaymentConfig entity) {
        ReadablePaymentConfiguration readable = new ReadablePaymentConfiguration();
        readable.setCode(entity.getCode());
        readable.setName(entity.getName());
        readable.setActive(entity.getActive());
        return readable;
    }
}
