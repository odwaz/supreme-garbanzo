package com.spaza.payment.service;

import com.spaza.payment.model.*;
import com.spaza.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public ReadableTransaction init(String cartCode, PersistablePayment payment) {
        ReadableTransaction transaction = new ReadableTransaction();
        transaction.setOrderId(cartCode);
        transaction.setAmount(payment.getAmount());
        transaction.setStatus("PENDING");
        return paymentRepository.save(transaction);
    }

    public ReadableTransaction authorize(Long orderId) {
        ReadableTransaction transaction = new ReadableTransaction();
        transaction.setOrderId(String.valueOf(orderId));
        transaction.setAmount(100.00);
        transaction.setStatus("AUTHORIZED");
        return paymentRepository.save(transaction);
    }

    public ReadableTransaction capture(Long orderId) {
        ReadableTransaction transaction = new ReadableTransaction();
        transaction.setOrderId(String.valueOf(orderId));
        transaction.setAmount(100.00);
        transaction.setStatus("CAPTURED");
        return paymentRepository.save(transaction);
    }

    public ReadableTransaction refund(Long orderId) {
        ReadableTransaction transaction = new ReadableTransaction();
        transaction.setOrderId(String.valueOf(orderId));
        transaction.setAmount(100.00);
        transaction.setStatus("REFUNDED");
        return paymentRepository.save(transaction);
    }

    public ReadableTransaction[] listTransactions(Long orderId) {
        List<ReadableTransaction> transactions = paymentRepository.findByOrderId(String.valueOf(orderId));
        return transactions.toArray(new ReadableTransaction[0]);
    }

    public String nextTransaction(Long orderId) {
        return "CAPTURE";
    }

    public Object[] getPaymentModules() {
        return new Object[]{"STRIPE", "PAYPAL", "SQUARE"};
    }

    public Object[] getPaymentModule(String code) {
        return new Object[]{Map.of("code", code, "name", code + " Payment", "enabled", true)};
    }

    public void configure(IntegrationModuleConfiguration configuration) {
        // Configuration logic
    }
}