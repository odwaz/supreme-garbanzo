package com.spaza.payment.service;

import com.spaza.payment.exception.*;
import com.spaza.payment.model.*;
import com.spaza.payment.repository.MerchantPaymentConfigRepository;
import com.spaza.payment.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class PaymentService {

    private static final String PENDING = "PENDING";
    private static final String AUTHORIZED = "AUTHORIZED";
    private static final String CAPTURED = "CAPTURED";
    private static final String ENABLED = "enabled";
    private static final String STRIPE = "STRIPE";
    private static final String DESCRIPTION = "description";

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private MerchantPaymentConfigRepository merchantPaymentConfigRepository;
    
    @Autowired
    private OzowPaymentModule ozowPaymentModule;
    
    @Autowired
    private StripePaymentModule stripePaymentModule;

    @Transactional
    public ReadableTransaction init(String cartCode, PersistablePayment payment) {
        if (paymentRepository.existsByOrderIdAndStatus(cartCode, PENDING)) {
            List<ReadableTransaction> pending = paymentRepository.findByOrderIdAndStatus(cartCode, PENDING);
            log.debug("Returning existing pending transaction for order: {}", cartCode);
            return pending.isEmpty() ? null : pending.get(0);
        }

        ReadableTransaction transaction = new ReadableTransaction();
        transaction.setOrderId(cartCode);
        transaction.setAmount(payment.getAmount());
        transaction.setStatus(PENDING);
        transaction.setPaymentMethod(payment.getPaymentMethod());
        ReadableTransaction saved = paymentRepository.save(transaction);
        log.info("Payment transaction initialized: {} for order: {}", payment.getPaymentMethod(), cartCode);
        return saved;
    }

    @Transactional
    public ReadableTransaction authorize(Long orderId) {
        List<ReadableTransaction> pending = paymentRepository.findByOrderIdAndStatus(
            String.valueOf(orderId), PENDING
        );
        
        if (pending.isEmpty()) {
            throw new PaymentFailedException("No pending payment found for order: " + orderId);
        }
        
        ReadableTransaction transaction = pending.get(0);
        transaction.setStatus(AUTHORIZED);
        ReadableTransaction saved = paymentRepository.save(transaction);
        log.info("Payment authorized for order: {}", orderId);
        return saved;
    }

    @Transactional
    public ReadableTransaction capture(Long orderId) {
        List<ReadableTransaction> authorized = paymentRepository.findByOrderIdAndStatus(
            String.valueOf(orderId), AUTHORIZED
        );
        
        if (authorized.isEmpty()) {
            throw new PaymentFailedException("No authorized payment found for order: " + orderId);
        }
        
        ReadableTransaction transaction = authorized.get(0);
        transaction.setStatus(CAPTURED);
        ReadableTransaction saved = paymentRepository.save(transaction);
        log.info("Payment captured for order: {}", orderId);
        return saved;
    }

    @Transactional
    public ReadableTransaction refund(Long orderId) {
        List<ReadableTransaction> captured = paymentRepository.findByOrderIdAndStatus(
            String.valueOf(orderId), CAPTURED
        );
        
        if (captured.isEmpty()) {
            throw new PaymentFailedException("No captured payment found for order: " + orderId);
        }
        
        ReadableTransaction refund = new ReadableTransaction();
        refund.setOrderId(String.valueOf(orderId));
        refund.setAmount(captured.get(0).getAmount());
        refund.setStatus("REFUNDED");
        refund.setPaymentMethod(captured.get(0).getPaymentMethod());
        
        ReadableTransaction saved = paymentRepository.save(refund);
        log.info("Payment refunded for order: {}", orderId);
        return saved;
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
            case PENDING: return "AUTHORIZE";
            case AUTHORIZED: return "CAPTURE";
            case CAPTURED: return "REFUND";
            default: return "NONE";
        }
    }

    public Object[] getPaymentModules(Long merchantId) {
        List<Object> modules = new ArrayList<>();
        
        modules.add(Map.of("code", "CASH", "name", "Cash Payment", ENABLED, true));
        modules.add(Map.of("code", "EFT", "name", "Electronic Funds Transfer", ENABLED, true));
        modules.add(Map.of("code", "CARD", "name", "Card on Delivery", ENABLED, true));
        modules.add(Map.of("code", "COD", "name", "Cash on Delivery", ENABLED, true));
        
        if (merchantId != null) {
            merchantPaymentConfigRepository.findByMerchantIdAndEnabled(merchantId, true)
                .forEach(config -> {
                    if ("OZOW".equals(config.getPaymentMethod())) {
                        modules.add(Map.of("code", "OZOW", "name", "Ozow Instant EFT", ENABLED, true));
                    } else if (STRIPE.equals(config.getPaymentMethod())) {
                        modules.add(Map.of("code", STRIPE, "name", "Stripe Payment", ENABLED, true));
                    }
                });
        }
        
        return modules.toArray();
    }

    public Object[] getPaymentModule(String code, Long merchantId) {
        Map<String, Object> module = new HashMap<>();
        module.put("code", code);
        module.put(ENABLED, true);
        
        switch (code) {
            case "CASH":
                module.put("name", "Cash Payment");
                module.put(DESCRIPTION, "Pay with cash at store");
                break;
            case "EFT":
                module.put("name", "Electronic Funds Transfer");
                module.put(DESCRIPTION, "Bank transfer");
                break;
            case "CARD":
                module.put("name", "Card on Delivery");
                module.put(DESCRIPTION, "Pay with card when delivered");
                break;
            case "COD":
                module.put("name", "Cash on Delivery");
                module.put(DESCRIPTION, "Pay cash when delivered");
                break;
            case "OZOW":
                if (merchantId != null) {
                    merchantPaymentConfigRepository.findByMerchantIdAndPaymentMethod(merchantId, "OZOW")
                        .ifPresent(config -> {
                            module.put("name", "Ozow Instant EFT");
                            module.put(DESCRIPTION, "Instant bank payment via Ozow");
                            module.put(ENABLED, config.getEnabled());
                        });
                }
                break;
            case STRIPE:
                if (merchantId != null) {
                    merchantPaymentConfigRepository.findByMerchantIdAndPaymentMethod(merchantId, STRIPE)
                        .ifPresent(config -> {
                            module.put("name", "Stripe Payment");
                            module.put(DESCRIPTION, "Credit/Debit card payment via Stripe");
                            module.put(ENABLED, config.getEnabled());
                        });
                }
                break;
            default:
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
        log.info("Configuring payment module: {}", configuration.getCode());
    }
}