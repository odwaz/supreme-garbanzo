package com.spaza.payment.service;

import com.spaza.payment.model.MerchantPaymentConfig;
import com.spaza.payment.repository.MerchantPaymentConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

@Component
public class StripePaymentModule {
    
    @Autowired
    private MerchantPaymentConfigRepository configRepository;
    
    private RestTemplate restTemplate = new RestTemplate();
    
    public Map<String, String> createPaymentIntent(Long merchantId, String orderId, Double amount) {
        MerchantPaymentConfig config = configRepository
            .findByMerchantIdAndPaymentMethod(merchantId, "STRIPE")
            .orElseThrow(() -> new RuntimeException("Stripe not configured for merchant"));
        
        if (!config.getEnabled()) {
            throw new RuntimeException("Stripe payment disabled for merchant");
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + config.getApiKey());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        String body = "amount=" + (int)(amount * 100) + 
                     "&currency=zar" +
                     "&metadata[order_id]=" + orderId;
        
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.stripe.com/v1/payment_intents",
                request,
                Map.class
            );
            
            Map<String, String> result = new HashMap<>();
            result.put("clientSecret", (String) response.getBody().get("client_secret"));
            result.put("paymentIntentId", (String) response.getBody().get("id"));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Stripe payment intent", e);
        }
    }
    
    public String capturePayment(Long merchantId, String paymentIntentId) {
        MerchantPaymentConfig config = configRepository
            .findByMerchantIdAndPaymentMethod(merchantId, "STRIPE")
            .orElseThrow(() -> new RuntimeException("Stripe not configured for merchant"));
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + config.getApiKey());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        HttpEntity<String> request = new HttpEntity<>("", headers);
        
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.stripe.com/v1/payment_intents/" + paymentIntentId + "/capture",
                request,
                Map.class
            );
            return (String) response.getBody().get("status");
        } catch (Exception e) {
            throw new RuntimeException("Failed to capture Stripe payment", e);
        }
    }
    
    public String refundPayment(Long merchantId, String paymentIntentId, Double amount) {
        MerchantPaymentConfig config = configRepository
            .findByMerchantIdAndPaymentMethod(merchantId, "STRIPE")
            .orElseThrow(() -> new RuntimeException("Stripe not configured for merchant"));
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + config.getApiKey());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        String body = "payment_intent=" + paymentIntentId;
        if (amount != null) {
            body += "&amount=" + (int)(amount * 100);
        }
        
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.stripe.com/v1/refunds",
                request,
                Map.class
            );
            return (String) response.getBody().get("status");
        } catch (Exception e) {
            throw new RuntimeException("Failed to refund Stripe payment", e);
        }
    }
    
    public boolean verifyWebhookSignature(String payload, String signature, String webhookSecret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return MessageDigest.isEqual(hexString.toString().getBytes(), signature.getBytes());
        } catch (Exception e) {
            return false;
        }
    }
}
