package com.spaza.customer.api;

import com.spaza.customer.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface ReferenceApiCtrl {

    @GetMapping("/api/v1/country")
    ResponseEntity<ReadableCountry[]> listCountries();

    @GetMapping("/api/v1/currency")
    ResponseEntity<ReadableCurrency[]> listCurrencies();

    @GetMapping("/api/v1/languages")
    ResponseEntity<ReadableLanguage[]> listLanguages();

    @GetMapping("/api/v1/measures")
    ResponseEntity<ReadableMeasure[]> listMeasures();

    @GetMapping("/api/v1/zones")
    ResponseEntity<ReadableZone[]> listZones(@RequestParam(required = false) String country);
}
