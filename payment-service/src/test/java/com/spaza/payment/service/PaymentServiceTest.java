package com.spaza.payment.service;

import com.spaza.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class PaymentServiceTest {

    @MockBean
    private PaymentService paymentService;

    @Test
    void testPaymentServiceExists() {
        // Basic test to ensure service can be mocked
        assert paymentService != null;
    }
}
