package com.spaza.tax.controller;

import com.spaza.tax.entity.TaxClass;
import com.spaza.tax.entity.TaxRate;
import com.spaza.tax.service.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/private/tax")

public class TaxController {
    
    @Autowired
    private TaxService service;
    
    @PostMapping("/classes")
    public ResponseEntity<TaxClass> createTaxClass(@RequestBody TaxClass taxClass) {
        return ResponseEntity.ok(service.saveTaxClass(taxClass));
    }
    
    @PutMapping("/classes/{id}")
    public ResponseEntity<TaxClass> updateTaxClass(@PathVariable Long id, @RequestBody TaxClass taxClass) {
        taxClass.setId(id);
        return ResponseEntity.ok(service.saveTaxClass(taxClass));
    }
    
    @DeleteMapping("/classes/{id}")
    public ResponseEntity<Void> deleteTaxClass(@PathVariable Long id) {
        service.deleteTaxClass(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/classes")
    public ResponseEntity<List<TaxClass>> listTaxClasses(@RequestParam Long merchantId) {
        return ResponseEntity.ok(service.findTaxClassesByMerchantId(merchantId));
    }
    
    @PostMapping("/rates")
    public ResponseEntity<TaxRate> createTaxRate(@RequestBody TaxRate taxRate) {
        return ResponseEntity.ok(service.saveTaxRate(taxRate));
    }
    
    @PutMapping("/rates/{id}")
    public ResponseEntity<TaxRate> updateTaxRate(@PathVariable Long id, @RequestBody TaxRate taxRate) {
        taxRate.setId(id);
        return ResponseEntity.ok(service.saveTaxRate(taxRate));
    }
    
    @DeleteMapping("/rates/{id}")
    public ResponseEntity<Void> deleteTaxRate(@PathVariable Long id) {
        service.deleteTaxRate(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/rates")
    public ResponseEntity<List<TaxRate>> listTaxRates(
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) Long taxClassId) {
        if (taxClassId != null) {
            return ResponseEntity.ok(service.findTaxRatesByTaxClassId(taxClassId));
        }
        return ResponseEntity.ok(service.findTaxRatesByMerchantId(merchantId));
    }
}
