package com.spaza.payment.controller;

import com.spaza.payment.model.ReadableTransaction;
import com.spaza.payment.repository.PaymentRepository;
import com.spaza.payment.model.StripeEventLog;
import com.spaza.payment.repository.StripeEventLogRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment/stripe")
public class StripeWebhookController {

    private static final Logger log = LoggerFactory.getLogger(StripeWebhookController.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StripeEventLogRepository stripeEventLogRepository;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/webhook")
    @Transactional
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        log.info("Received Stripe webhook request");
        
        final Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            log.info("Webhook signature verified successfully. Event type: {}, Event ID: {}", 
                    event.getType(), event.getId());
        } catch (SignatureVerificationException e) {
            log.error("Stripe webhook signature verification failed: {}", e.getMessage());
            return ResponseEntity.status(400).body("Invalid signature");
        } catch (Exception e) {
            log.error("Error processing Stripe webhook", e);
            return ResponseEntity.status(500).body("Internal server error");
        }

        String eventType = event.getType();
        String orderId = null;
        String eventId = event.getId();

        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        if (deserializer != null && deserializer.getObject().isPresent()) {
            Object obj = deserializer.getObject().get();
            if (obj instanceof PaymentIntent) {
                PaymentIntent pi = (PaymentIntent) obj;
                Map<String, String> metadata = pi.getMetadata();
                if (metadata != null) {
                    orderId = metadata.get("order_id");
                }
            }
        }

        if (orderId == null || orderId.isBlank()) {
            log.warn("Webhook event {} missing order_id in metadata. Event ID: {}", 
                    eventType, eventId);
            return ResponseEntity.badRequest().body("Missing order_id in metadata");
        }
        
        log.info("Processing webhook for order_id: {}, event type: {}", orderId, eventType);

        // Idempotency: skip if this Stripe event was already processed
        if (eventId != null && stripeEventLogRepository.existsByEventId(eventId)) {
            log.info("Duplicate webhook event detected. Event ID: {} already processed", eventId);
            return ResponseEntity.ok("Ignored (duplicate)");
        }

        List<ReadableTransaction> transactions = paymentRepository.findByOrderId(orderId);
        if (transactions.isEmpty()) {
            log.error("Transaction not found for order_id: {}", orderId);
            return ResponseEntity.badRequest().body("Transaction not found");
        }

        ReadableTransaction transaction = transactions.get(0);

        switch (eventType) {
            case "payment_intent.succeeded":
                transaction.setStatus("CAPTURED");
                log.info("Payment succeeded for order_id: {}, transaction updated to CAPTURED", orderId);
                break;
            case "payment_intent.payment_failed":
                transaction.setStatus("FAILED");
                log.warn("Payment failed for order_id: {}, transaction updated to FAILED", orderId);
                break;
            case "payment_intent.canceled":
                transaction.setStatus("CANCELLED");
                log.info("Payment canceled for order_id: {}, transaction updated to CANCELLED", orderId);
                break;
            default:
                log.debug("Ignoring unhandled event type: {}", eventType);
                return ResponseEntity.ok("Ignored");
        }

        // Record the event id to prevent duplicates; handle race by catching unique constraint violation
        if (eventId != null) {
            try {
                StripeEventLog eventLog = new StripeEventLog();
                eventLog.setEventId(eventId);
                eventLog.setOrderId(orderId);
                stripeEventLogRepository.save(eventLog);
                log.debug("Saved event log for event ID: {}", eventId);
            } catch (DataIntegrityViolationException e) {
                log.warn("Duplicate event detected during save. Event ID: {} already exists", eventId);
                return ResponseEntity.ok("Ignored (duplicate)");
            }
        }

        paymentRepository.save(transaction);
        log.info("Successfully processed webhook for order_id: {}, event type: {}", orderId, eventType);
        return ResponseEntity.ok("OK");
    }
}
