package com.spaza.payment.service;

import com.spaza.payment.model.MerchantPaymentConfig;
import com.spaza.payment.repository.MerchantPaymentConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

@Component
public class OzowPaymentModule {

    private static final String OZOW = "OZOW";
    private static final String OZOW_NOT_CONFIGURED = "Ozow not configured for merchant";
    
    @Autowired
    private MerchantPaymentConfigRepository configRepository;
    
    @Value("${ozow.api.url:https://api.ozow.com}")
    private String ozowApiUrl;
    
    @Value("${ozow.callback.url}")
    private String callbackUrl;
    
    private RestTemplate restTemplate = new RestTemplate();
    
    public String initiatePayment(Long merchantId, String orderId, Double amount) {
        MerchantPaymentConfig config = configRepository
            .findByMerchantIdAndPaymentMethod(merchantId, OZOW)
            .orElseThrow(() -> new IllegalStateException(OZOW_NOT_CONFIGURED));
        
        if (Boolean.FALSE.equals(config.getEnabled())) {
            throw new IllegalStateException("Ozow payment disabled for merchant");
        }
        
        String hash = generateHash(config.getSiteCode(), orderId, amount, config.getPrivateKey());
        
        Map<String, String> params = new HashMap<>();
        params.put("SiteCode", config.getSiteCode());
        params.put("TransactionReference", orderId);
        params.put("Amount", String.format("%.2f", amount));
        params.put("CurrencyCode", "ZAR");
        params.put("IsTest", "false");
        params.put("SuccessUrl", callbackUrl + "/success");
        params.put("CancelUrl", callbackUrl + "/cancel");
        params.put("ErrorUrl", callbackUrl + "/error");
        params.put("NotifyUrl", callbackUrl + "/notify");
        params.put("HashCheck", hash);
        
        return ozowApiUrl + "/PostPaymentRequest?" + buildQueryString(params);
    }
    
    public boolean verifyCallback(Map<String, String> params, String receivedHash, String privateKey) {
        String calculatedHash = generateCallbackHash(params, privateKey);
        return calculatedHash.equals(receivedHash);
    }
    
    private String generateHash(String siteCode, String transactionRef, Double amount, String privateKey) {
        String input = siteCode + transactionRef + String.format("%.2f", amount) + privateKey;
        return sha512(input);
    }
    
    private String generateCallbackHash(Map<String, String> params, String privateKey) {
        String input = params.get("SiteCode") + params.get("TransactionId") + 
                      params.get("TransactionReference") + params.get("Amount") + 
                      params.get("Status") + privateKey;
        return sha512(input);
    }
    
    private String sha512(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hash = md.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString().toLowerCase();
        } catch (Exception e) {
            throw new IllegalStateException("Hash generation failed", e);
        }
    }
    
    private String buildQueryString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        params.forEach((key, value) -> {
            if (sb.length() > 0) sb.append("&");
            sb.append(key).append("=").append(value);
        });
        return sb.toString();
    }
}
