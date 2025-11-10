package com.spaza.customer.controller;

import com.spaza.customer.api.ReferenceApiCtrl;
import com.spaza.customer.delegate.ReferenceApiCtrlDelegate;
import com.spaza.customer.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class ReferenceApiCtrlController implements ReferenceApiCtrl {

    @Autowired
    private ReferenceApiCtrlDelegate delegate;

    @Override
    public ResponseEntity<ReadableCountry[]> listCountries() {
        return delegate.listCountries();
    }

    @Override
    public ResponseEntity<ReadableCountry> getCountry(String code) {
        return delegate.getCountry(code);
    }

    @Override
    public ResponseEntity<ReadableCurrency[]> listCurrencies() {
        return delegate.listCurrencies();
    }

    @Override
    public ResponseEntity<ReadableCurrency> getCurrency(String code) {
        return delegate.getCurrency(code);
    }

    @Override
    public ResponseEntity<ReadableLanguage[]> listLanguages() {
        return delegate.listLanguages();
    }

    @Override
    public ResponseEntity<ReadableLanguage> getLanguage(String code) {
        return delegate.getLanguage(code);
    }

    @Override
    public ResponseEntity<ReadableMeasure[]> listMeasures() {
        return delegate.listMeasures();
    }
}
