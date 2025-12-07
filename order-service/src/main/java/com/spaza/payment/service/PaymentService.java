package com.spaza.payment.service;

import com.spaza.payment.model.*;
import com.spaza.payment.repository.MerchantPaymentConfigRepository;
import com.spaza.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private MerchantPaymentConfigRepository merchantPaymentConfigRepository;
    
    @Autowired
    private OzowPaymentModule ozowPaymentModule;
    
    @Autowired
    private StripePaymentModule stripePaymentModule;

    public ReadableTransaction init(String cartCode, PersistablePayment payment) {
        ReadableTransaction transaction = new ReadableTransaction();
        transaction.setOrderId(cartCode);
        transaction.setAmount(payment.getAmount());
        transaction.setStatus("PENDING");
        transaction.setPaymentMethod(payment.getPaymentMethod());
        return paymentRepository.save(transaction);
    }

    public ReadableTransaction authorize(Long orderId) {
        List<ReadableTransaction> pending = paymentRepository.findByOrderIdAndStatus(
            String.valueOf(orderId), "PENDING"
        );
        
        if (pending.isEmpty()) {
            throw new RuntimeException("No pending payment found");
        }
        
        ReadableTransaction transaction = pending.get(0);
        transaction.setStatus("AUTHORIZED");
        return paymentRepository.save(transaction);
    }

    public ReadableTransaction capture(Long orderId) {
        List<ReadableTransaction> authorized = paymentRepository.findByOrderIdAndStatus(
            String.valueOf(orderId), "AUTHORIZED"
        );
        
        if (authorized.isEmpty()) {
            throw new RuntimeException("No authorized payment found");
        }
        
        ReadableTransaction transaction = authorized.get(0);
        transaction.setStatus("CAPTURED");
        return paymentRepository.save(transaction);
    }

    public ReadableTransaction refund(Long orderId) {
        List<ReadableTransaction> captured = paymentRepository.findByOrderIdAndStatus(
            String.valueOf(orderId), "CAPTURED"
        );
        
        if (captured.isEmpty()) {
            throw new RuntimeException("No captured payment found");
        }
        
        ReadableTransaction refund = new ReadableTransaction();
        refund.setOrderId(String.valueOf(orderId));
        refund.setAmount(captured.get(0).getAmount());
        refund.setStatus("REFUNDED");
        refund.setPaymentMethod(captured.get(0).getPaymentMethod());
        
        return paymentRepository.save(refund);
    }

    public ReadableTransaction[] listTransactions(Long orderId) {
        List<ReadableTransaction> transactions = paymentRepository.findByOrderId(String.valueOf(orderId));
        return transactions.toArray(new ReadableTransaction[0]);
    }

    public String nextTransaction(Long orderId) {
        List<ReadableTransaction> transactions = paymentRepository.findByOrderId(String.valueOf(orderId));
        
        if (transactions.isEmpty()) {
            return "INIT";
        }
        
        ReadableTransaction latest = transactions.get(transactions.size() - 1);
        
        switch (latest.getStatus()) {
            case "PENDING": return "AUTHORIZE";
            case "AUTHORIZED": return "CAPTURE";
            case "CAPTURED": return "REFUND";
            default: return "NONE";
        }
    }

    public Object[] getPaymentModules(Long merchantId) {
        List<Object> modules = new ArrayList<>();
        
        modules.add(Map.of("code", "CASH", "name", "Cash Payment", "enabled", true));
        modules.add(Map.of("code", "EFT", "name", "Electronic Funds Transfer", "enabled", true));
        modules.add(Map.of("code", "CARD", "name", "Card on Delivery", "enabled", true));
        modules.add(Map.of("code", "COD", "name", "Cash on Delivery", "enabled", true));
        
        if (merchantId != null) {
            merchantPaymentConfigRepository.findByMerchantIdAndEnabled(merchantId, true)
                .forEach(config -> {
                    if ("OZOW".equals(config.getPaymentMethod())) {
                        modules.add(Map.of("code", "OZOW", "name", "Ozow Instant EFT", "enabled", true));
                    } else if ("STRIPE".equals(config.getPaymentMethod())) {
                        modules.add(Map.of("code", "STRIPE", "name", "Stripe Payment", "enabled", true));
                    }
                });
        }
        
        return modules.toArray();
    }

    public Object[] getPaymentModule(String code, Long merchantId) {
        Map<String, Object> module = new HashMap<>();
        module.put("code", code);
        module.put("enabled", true);
        
        switch (code) {
            case "CASH":
                module.put("name", "Cash Payment");
                module.put("description", "Pay with cash at store");
                break;
            case "EFT":
                module.put("name", "Electronic Funds Transfer");
                module.put("description", "Bank transfer");
                break;
            case "CARD":
                module.put("name", "Card on Delivery");
                module.put("description", "Pay with card when delivered");
                break;
            case "COD":
                module.put("name", "Cash on Delivery");
                module.put("description", "Pay cash when delivered");
                break;
            case "OZOW":
                if (merchantId != null) {
                    merchantPaymentConfigRepository.findByMerchantIdAndPaymentMethod(merchantId, "OZOW")
                        .ifPresent(config -> {
                            module.put("name", "Ozow Instant EFT");
                            module.put("description", "Instant bank payment via Ozow");
                            module.put("enabled", config.getEnabled());
                        });
                }
                break;
            case "STRIPE":
                if (merchantId != null) {
                    merchantPaymentConfigRepository.findByMerchantIdAndPaymentMethod(merchantId, "STRIPE")
                        .ifPresent(config -> {
                            module.put("name", "Stripe Payment");
                            module.put("description", "Credit/Debit card payment via Stripe");
                            module.put("enabled", config.getEnabled());
                        });
                }
                break;
        }
        
        return new Object[]{module};
    }
    
    public String initiateOzowPayment(Long merchantId, String orderId, Double amount) {
        return ozowPaymentModule.initiatePayment(merchantId, orderId, amount);
    }
    
    public Map<String, String> initiateStripePayment(Long merchantId, String orderId, Double amount) {
        return stripePaymentModule.createPaymentIntent(merchantId, orderId, amount);
    }

    public void configure(IntegrationModuleConfiguration configuration) {
        System.out.println("Configuring payment module: " + configuration.getCode());
    }
}