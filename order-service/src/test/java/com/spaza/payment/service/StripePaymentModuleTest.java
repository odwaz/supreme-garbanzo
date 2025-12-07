package com.spaza.payment.service;

import com.spaza.payment.model.MerchantPaymentConfig;
import com.spaza.payment.repository.MerchantPaymentConfigRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StripePaymentModuleTest {

    @Mock
    private MerchantPaymentConfigRepository configRepository;

    @InjectMocks
    private StripePaymentModule module;

    @Test
    void shouldThrowExceptionWhenStripeNotConfigured() {
        when(configRepository.findByMerchantIdAndPaymentMethod(1L, "STRIPE"))
            .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            module.createPaymentIntent(1L, "order123", 150.0);
        });
    }

    @Test
    void shouldThrowExceptionWhenStripeDisabled() {
        MerchantPaymentConfig config = new MerchantPaymentConfig();
        config.setEnabled(false);

        when(configRepository.findByMerchantIdAndPaymentMethod(1L, "STRIPE"))
            .thenReturn(Optional.of(config));

        assertThrows(RuntimeException.class, () -> {
            module.createPaymentIntent(1L, "order123", 150.0);
        });
    }
}
