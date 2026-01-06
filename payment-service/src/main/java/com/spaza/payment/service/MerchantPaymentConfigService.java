package com.spaza.payment.service;

import com.spaza.payment.model.*;
import com.spaza.payment.repository.MerchantPaymentConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MerchantPaymentConfigService {
    
    @Autowired
    private MerchantPaymentConfigRepository repository;
    
    public ReadableMerchantPaymentConfig saveConfig(PersistableMerchantPaymentConfig config) {
        MerchantPaymentConfig entity = repository
            .findByMerchantIdAndPaymentMethod(config.getMerchantId(), config.getPaymentMethod())
            .orElse(new MerchantPaymentConfig());
        
        entity.setMerchantId(config.getMerchantId());
        entity.setPaymentMethod(config.getPaymentMethod());
        entity.setEnabled(config.getEnabled());
        entity.setApiKey(config.getApiKey());
        entity.setSiteCode(config.getSiteCode());
        entity.setPrivateKey(config.getPrivateKey());
        entity.setConfigJson(config.getConfigJson());
        
        if (entity.getId() == null) {
            entity.setDateCreated(LocalDateTime.now());
        }
        entity.setDateModified(LocalDateTime.now());
        
        return toReadable(repository.save(entity));
    }
    
    public List<ReadableMerchantPaymentConfig> getConfigs(Long merchantId) {
        return repository.findByMerchantId(merchantId).stream()
            .map(this::toReadable)
            .collect(Collectors.toList());
    }
    
    public ReadableMerchantPaymentConfig getConfig(Long merchantId, String paymentMethod) {
        return repository.findByMerchantIdAndPaymentMethod(merchantId, paymentMethod)
            .map(this::toReadable)
            .orElse(null);
    }
    
    public void deleteConfig(Long merchantId, String paymentMethod) {
        repository.findByMerchantIdAndPaymentMethod(merchantId, paymentMethod)
            .ifPresent(repository::delete);
    }
    
    private ReadableMerchantPaymentConfig toReadable(MerchantPaymentConfig entity) {
        ReadableMerchantPaymentConfig readable = new ReadableMerchantPaymentConfig();
        readable.setId(entity.getId());
        readable.setMerchantId(entity.getMerchantId());
        readable.setPaymentMethod(entity.getPaymentMethod());
        readable.setEnabled(entity.getEnabled());
        readable.setSiteCode(entity.getSiteCode());
        return readable;
    }
}
