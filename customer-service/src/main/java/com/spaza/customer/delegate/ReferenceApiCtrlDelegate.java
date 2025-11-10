package com.spaza.customer.delegate;

import com.spaza.customer.model.*;
import org.springframework.http.ResponseEntity;

public interface ReferenceApiCtrlDelegate {
    ResponseEntity<ReadableCountry[]> listCountries();
    ResponseEntity<ReadableCountry> getCountry(String code);
    ResponseEntity<ReadableCurrency[]> listCurrencies();
    ResponseEntity<ReadableCurrency> getCurrency(String code);
    ResponseEntity<ReadableLanguage[]> listLanguages();
    ResponseEntity<ReadableLanguage> getLanguage(String code);
    ResponseEntity<ReadableMeasure[]> listMeasures();
}
