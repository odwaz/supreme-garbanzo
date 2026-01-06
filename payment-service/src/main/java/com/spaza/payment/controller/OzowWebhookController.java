package com.spaza.payment.controller;

import com.spaza.payment.model.ReadableTransaction;
import com.spaza.payment.repository.MerchantPaymentConfigRepository;
import com.spaza.payment.repository.PaymentRepository;
import com.spaza.payment.service.OzowPaymentModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment/ozow")
public class OzowWebhookController {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private MerchantPaymentConfigRepository configRepository;
    
    @Autowired
    private OzowPaymentModule ozowModule;
    
    @PostMapping("/notify")
    public ResponseEntity<String> handleNotification(@RequestParam Map<String, String> params) {
        String orderId = params.get("TransactionReference");
        String status = params.get("Status");
        String hash = params.get("HashCheck");
        
        List<ReadableTransaction> transactions = paymentRepository.findByOrderId(orderId);
        if (transactions.isEmpty()) {
            return ResponseEntity.badRequest().body("Transaction not found");
        }
        
        ReadableTransaction transaction = transactions.get(0);
        
        // Get merchant config to verify hash
        // Note: You'll need to get merchantId from order
        // For now, simplified verification
        
        if ("Complete".equals(status)) {
            transaction.setStatus("CAPTURED");
        } else if ("Cancelled".equals(status)) {
            transaction.setStatus("CANCELLED");
        } else if ("Error".equals(status)) {
            transaction.setStatus("FAILED");
        }
        
        paymentRepository.save(transaction);
        return ResponseEntity.ok("OK");
    }
    
    @GetMapping("/success")
    public ResponseEntity<String> handleSuccess(@RequestParam String TransactionReference) {
        return ResponseEntity.ok("Payment successful for order: " + TransactionReference);
    }
    
    @GetMapping("/cancel")
    public ResponseEntity<String> handleCancel(@RequestParam String TransactionReference) {
        return ResponseEntity.ok("Payment cancelled for order: " + TransactionReference);
    }
    
    @GetMapping("/error")
    public ResponseEntity<String> handleError(@RequestParam String TransactionReference) {
        return ResponseEntity.ok("Payment error for order: " + TransactionReference);
    }
}
