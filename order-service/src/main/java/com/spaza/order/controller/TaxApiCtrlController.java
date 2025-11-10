package com.spaza.order.controller;

import com.spaza.order.api.TaxApiCtrl;
import com.spaza.order.delegate.TaxApiCtrlDelegate;
import com.spaza.order.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class TaxApiCtrlController implements TaxApiCtrl {

    @Autowired
    private TaxApiCtrlDelegate delegate;

    @Override
    public ResponseEntity<ReadableTaxClass[]> listTaxClasses() {
        return delegate.listTaxClasses();
    }

    @Override
    public ResponseEntity<ReadableTaxClass> createTaxClass(PersistableTaxClass taxClass) {
        return delegate.createTaxClass(taxClass);
    }

    @Override
    public ResponseEntity<ReadableTaxRate[]> listTaxRates() {
        return delegate.listTaxRates();
    }

    @Override
    public ResponseEntity<ReadableTaxRate> createTaxRate(PersistableTaxRate taxRate) {
        return delegate.createTaxRate(taxRate);
    }

    @Override
    public ResponseEntity<Void> deleteTaxRate(Long id) {
        return delegate.deleteTaxRate(id);
    }
}
