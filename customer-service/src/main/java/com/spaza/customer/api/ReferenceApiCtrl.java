package com.spaza.customer.api;

import com.spaza.customer.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface ReferenceApiCtrl {

    @GetMapping("/api/v1/country")
    ResponseEntity<ReadableCountry[]> listCountries();

    @GetMapping("/api/v1/country/{code}")
    ResponseEntity<ReadableCountry> getCountry(@PathVariable String code);

    @GetMapping("/api/v1/currency")
    ResponseEntity<ReadableCurrency[]> listCurrencies();

    @GetMapping("/api/v1/currency/{code}")
    ResponseEntity<ReadableCurrency> getCurrency(@PathVariable String code);

    @GetMapping("/api/v1/languages")
    ResponseEntity<ReadableLanguage[]> listLanguages();

    @GetMapping("/api/v1/language/{code}")
    ResponseEntity<ReadableLanguage> getLanguage(@PathVariable String code);

    @GetMapping("/api/v1/measures")
    ResponseEntity<ReadableMeasure[]> listMeasures();
}
