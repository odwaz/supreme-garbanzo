package com.spaza.order.delegate;

import com.spaza.order.model.*;
import com.spaza.order.service.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaxApiCtrlDelegateImpl implements TaxApiCtrlDelegate {

    private final TaxService taxService;

    @Override
    public ResponseEntity<ReadableTaxClass[]> listTaxClasses() {
        return ResponseEntity.ok(taxService.listTaxClasses());
    }

    @Override
    public ResponseEntity<ReadableTaxClass> createTaxClass(PersistableTaxClass taxClass) {
        return ResponseEntity.ok(taxService.createTaxClass(taxClass));
    }

    @Override
    public ResponseEntity<ReadableTaxRate[]> listTaxRates() {
        return ResponseEntity.ok(taxService.listTaxRates());
    }

    @Override
    public ResponseEntity<ReadableTaxRate> createTaxRate(PersistableTaxRate taxRate) {
        return ResponseEntity.ok(taxService.createTaxRate(taxRate));
    }

    @Override
    public ResponseEntity<Void> deleteTaxRate(Long id) {
        taxService.deleteTaxRate(id);
        return ResponseEntity.ok().build();
    }
}
