package com.spaza.payment.service;

import com.spaza.payment.model.MerchantPaymentConfig;
import com.spaza.payment.repository.MerchantPaymentConfigRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OzowPaymentModuleTest {

    @Mock
    private MerchantPaymentConfigRepository configRepository;

    @InjectMocks
    private OzowPaymentModule module;

    @Test
    void shouldGeneratePaymentUrl() {
        MerchantPaymentConfig config = new MerchantPaymentConfig();
        config.setSiteCode("TEST123");
        config.setPrivateKey("test_private_key");
        config.setEnabled(true);

        when(configRepository.findByMerchantIdAndPaymentMethod(1L, "OZOW"))
            .thenReturn(Optional.of(config));

        ReflectionTestUtils.setField(module, "ozowApiUrl", "https://api.ozow.com");
        ReflectionTestUtils.setField(module, "callbackUrl", "http://localhost:8087/api/v1/payment/ozow");

        String url = module.initiatePayment(1L, "order123", 150.0);

        assertNotNull(url);
        assertTrue(url.contains("TEST123"));
        assertTrue(url.contains("order123"));
    }

    @Test
    void shouldThrowExceptionWhenOzowNotConfigured() {
        when(configRepository.findByMerchantIdAndPaymentMethod(1L, "OZOW"))
            .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            module.initiatePayment(1L, "order123", 150.0);
        });
    }

    @Test
    void shouldThrowExceptionWhenOzowDisabled() {
        MerchantPaymentConfig config = new MerchantPaymentConfig();
        config.setEnabled(false);

        when(configRepository.findByMerchantIdAndPaymentMethod(1L, "OZOW"))
            .thenReturn(Optional.of(config));

        assertThrows(RuntimeException.class, () -> {
            module.initiatePayment(1L, "order123", 150.0);
        });
    }
}
