package com.spaza.order.delegate;

import com.spaza.order.model.*;
import org.springframework.http.ResponseEntity;

public interface TaxApiCtrlDelegate {
    ResponseEntity<ReadableTaxClass[]> listTaxClasses();
    ResponseEntity<ReadableTaxClass> createTaxClass(PersistableTaxClass taxClass);
    ResponseEntity<ReadableTaxRate[]> listTaxRates();
    ResponseEntity<ReadableTaxRate> createTaxRate(PersistableTaxRate taxRate);
    ResponseEntity<Void> deleteTaxRate(Long id);
}
