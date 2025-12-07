package com.spaza.payment.service;

import com.spaza.payment.model.*;
import com.spaza.payment.repository.MerchantPaymentConfigRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MerchantPaymentConfigServiceTest {

    @Mock
    private MerchantPaymentConfigRepository repository;

    @InjectMocks
    private MerchantPaymentConfigService service;

    @Test
    void shouldSaveOzowConfig() {
        PersistableMerchantPaymentConfig config = new PersistableMerchantPaymentConfig();
        config.setMerchantId(1L);
        config.setPaymentMethod("OZOW");
        config.setEnabled(true);
        config.setApiKey("test_key");
        config.setSiteCode("TEST123");
        config.setPrivateKey("test_private");

        MerchantPaymentConfig entity = new MerchantPaymentConfig();
        entity.setId(1L);
        entity.setMerchantId(1L);
        entity.setPaymentMethod("OZOW");
        entity.setEnabled(true);

        when(repository.findByMerchantIdAndPaymentMethod(1L, "OZOW")).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(entity);

        ReadableMerchantPaymentConfig result = service.saveConfig(config);

        assertNotNull(result);
        assertEquals(1L, result.getMerchantId());
        assertEquals("OZOW", result.getPaymentMethod());
        assertTrue(result.getEnabled());
        verify(repository).save(any());
    }

    @Test
    void shouldGetConfigByMerchantAndMethod() {
        MerchantPaymentConfig entity = new MerchantPaymentConfig();
        entity.setMerchantId(1L);
        entity.setPaymentMethod("OZOW");
        entity.setEnabled(true);

        when(repository.findByMerchantIdAndPaymentMethod(1L, "OZOW")).thenReturn(Optional.of(entity));

        ReadableMerchantPaymentConfig result = service.getConfig(1L, "OZOW");

        assertNotNull(result);
        assertEquals("OZOW", result.getPaymentMethod());
    }

    @Test
    void shouldDeleteConfig() {
        MerchantPaymentConfig entity = new MerchantPaymentConfig();
        when(repository.findByMerchantIdAndPaymentMethod(1L, "OZOW")).thenReturn(Optional.of(entity));

        service.deleteConfig(1L, "OZOW");

        verify(repository).delete(entity);
    }
}
