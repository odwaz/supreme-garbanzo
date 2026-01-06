package com.spaza.payment.service;

import com.spaza.payment.model.*;
import com.spaza.payment.repository.MerchantPaymentConfigRepository;
import com.spaza.payment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private MerchantPaymentConfigRepository merchantPaymentConfigRepository;

    @Mock
    private OzowPaymentModule ozowPaymentModule;

    @InjectMocks
    private PaymentService service;

    @Test
    void shouldReturnDefaultPaymentModulesWithoutMerchant() {
        Object[] modules = service.getPaymentModules(null);

        assertEquals(4, modules.length);
    }

    @Test
    void shouldIncludeOzowWhenEnabledForMerchant() {
        MerchantPaymentConfig config = new MerchantPaymentConfig();
        config.setMerchantId(1L);
        config.setPaymentMethod("OZOW");
        config.setEnabled(true);

        when(merchantPaymentConfigRepository.findByMerchantIdAndEnabled(1L, true))
            .thenReturn(Arrays.asList(config));

        Object[] modules = service.getPaymentModules(1L);

        assertEquals(5, modules.length);
    }

    @Test
    void shouldIncludeStripeWhenEnabledForMerchant() {
        MerchantPaymentConfig config = new MerchantPaymentConfig();
        config.setMerchantId(1L);
        config.setPaymentMethod("STRIPE");
        config.setEnabled(true);

        when(merchantPaymentConfigRepository.findByMerchantIdAndEnabled(1L, true))
            .thenReturn(Arrays.asList(config));

        Object[] modules = service.getPaymentModules(1L);

        assertEquals(5, modules.length);
    }

    @Test
    void shouldInitializePayment() {
        PersistablePayment payment = new PersistablePayment();
        payment.setAmount(100.0);
        payment.setPaymentMethod("CASH");

        ReadableTransaction transaction = new ReadableTransaction();
        transaction.setId(1L);
        transaction.setStatus("PENDING");

        when(paymentRepository.save(any())).thenReturn(transaction);

        ReadableTransaction result = service.init("cart123", payment);

        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        verify(paymentRepository).save(any());
    }

    @Test
    void shouldAuthorizePayment() {
        ReadableTransaction pending = new ReadableTransaction();
        pending.setId(1L);
        pending.setStatus("PENDING");

        when(paymentRepository.findByOrderIdAndStatus("1", "PENDING"))
            .thenReturn(Arrays.asList(pending));
        when(paymentRepository.save(any())).thenReturn(pending);

        ReadableTransaction result = service.authorize(1L);

        assertEquals("AUTHORIZED", result.getStatus());
    }

    @Test
    void shouldCapturePayment() {
        ReadableTransaction authorized = new ReadableTransaction();
        authorized.setId(1L);
        authorized.setStatus("AUTHORIZED");

        when(paymentRepository.findByOrderIdAndStatus("1", "AUTHORIZED"))
            .thenReturn(Arrays.asList(authorized));
        when(paymentRepository.save(any())).thenReturn(authorized);

        ReadableTransaction result = service.capture(1L);

        assertEquals("CAPTURED", result.getStatus());
    }
}
