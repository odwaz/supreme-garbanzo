package com.spaza.order.api;

import com.spaza.order.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

public interface TaxApiCtrl {

    @GetMapping("/api/v1/private/tax/class")
    ResponseEntity<ReadableTaxClass[]> listTaxClasses();

    @PostMapping("/api/v1/private/tax/class")
    ResponseEntity<ReadableTaxClass> createTaxClass(@Valid @RequestBody PersistableTaxClass taxClass);

    @GetMapping("/api/v1/private/tax/rates")
    ResponseEntity<ReadableTaxRate[]> listTaxRates();

    @PostMapping("/api/v1/private/tax/rates")
    ResponseEntity<ReadableTaxRate> createTaxRate(@Valid @RequestBody PersistableTaxRate taxRate);

    @DeleteMapping("/api/v1/private/tax/rates/{id}")
    ResponseEntity<Void> deleteTaxRate(@PathVariable Long id);
}
