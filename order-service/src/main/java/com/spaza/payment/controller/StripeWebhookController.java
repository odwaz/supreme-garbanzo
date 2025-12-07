package com.spaza.payment.controller;

import com.spaza.payment.repository.PaymentRepository;
import com.spaza.payment.model.ReadableTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment/stripe")
public class StripeWebhookController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        if (!verifySignature(payload, sigHeader, webhookSecret)) {
            return ResponseEntity.status(400).body("Invalid signature");
        }

        Map<String, Object> event = parseJson(payload);
        String eventType = (String) event.get("type");
        Map<String, Object> data = (Map<String, Object>) event.get("data");
        Map<String, Object> object = (Map<String, Object>) data.get("object");
        Map<String, String> metadata = (Map<String, String>) object.get("metadata");
        String orderId = metadata.get("order_id");

        List<ReadableTransaction> transactions = paymentRepository.findByOrderId(orderId);
        if (transactions.isEmpty()) {
            return ResponseEntity.badRequest().body("Transaction not found");
        }

        ReadableTransaction transaction = transactions.get(0);

        switch (eventType) {
            case "payment_intent.succeeded":
                transaction.setStatus("CAPTURED");
                break;
            case "payment_intent.payment_failed":
                transaction.setStatus("FAILED");
                break;
            case "payment_intent.canceled":
                transaction.setStatus("CANCELLED");
                break;
        }

        paymentRepository.save(transaction);
        return ResponseEntity.ok("OK");
    }

    private boolean verifySignature(String payload, String sigHeader, String secret) {
        try {
            String[] parts = sigHeader.split(",");
            long timestamp = 0;
            String signature = "";

            for (String part : parts) {
                String[] keyValue = part.split("=");
                if (keyValue[0].equals("t")) timestamp = Long.parseLong(keyValue[1]);
                if (keyValue[0].equals("v1")) signature = keyValue[1];
            }

            if (Math.abs(System.currentTimeMillis() / 1000 - timestamp) > 300) {
                return false;
            }

            String signedPayload = timestamp + "." + payload;
            String computed = computeHmacSha256(signedPayload, secret);
            return computed.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }

    private String computeHmacSha256(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(), "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes());
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    private Map<String, Object> parseJson(String json) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.readValue(json, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
