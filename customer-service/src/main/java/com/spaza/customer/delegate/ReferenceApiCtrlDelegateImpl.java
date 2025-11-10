package com.spaza.customer.delegate;

import com.spaza.customer.model.*;
import com.spaza.customer.service.ReferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReferenceApiCtrlDelegateImpl implements ReferenceApiCtrlDelegate {

    private final ReferenceService referenceService;

    @Override
    public ResponseEntity<ReadableCountry[]> listCountries() {
        return ResponseEntity.ok(referenceService.listCountries());
    }

    @Override
    public ResponseEntity<ReadableCountry> getCountry(String code) {
        return ResponseEntity.ok(referenceService.getCountry(code));
    }

    @Override
    public ResponseEntity<ReadableCurrency[]> listCurrencies() {
        return ResponseEntity.ok(referenceService.listCurrencies());
    }

    @Override
    public ResponseEntity<ReadableCurrency> getCurrency(String code) {
        return ResponseEntity.ok(referenceService.getCurrency(code));
    }

    @Override
    public ResponseEntity<ReadableLanguage[]> listLanguages() {
        return ResponseEntity.ok(referenceService.listLanguages());
    }

    @Override
    public ResponseEntity<ReadableLanguage> getLanguage(String code) {
        return ResponseEntity.ok(referenceService.getLanguage(code));
    }

    @Override
    public ResponseEntity<ReadableMeasure[]> listMeasures() {
        return ResponseEntity.ok(referenceService.listMeasures());
    }
}
