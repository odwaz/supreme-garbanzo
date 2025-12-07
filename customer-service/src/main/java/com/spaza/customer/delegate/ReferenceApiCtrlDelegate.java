package com.spaza.customer.delegate;

import com.spaza.customer.model.*;
import org.springframework.http.ResponseEntity;

public interface ReferenceApiCtrlDelegate {
    ResponseEntity<ReadableCountry[]> listCountries();
    ResponseEntity<ReadableCurrency[]> listCurrencies();
    ResponseEntity<ReadableLanguage[]> listLanguages();
    ResponseEntity<ReadableMeasure[]> listMeasures();
}
