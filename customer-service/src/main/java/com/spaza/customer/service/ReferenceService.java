package com.spaza.customer.service;

import com.spaza.customer.model.*;
import com.spaza.customer.repository.*;
import org.springframework.stereotype.Service;

@Service
public class ReferenceService {

    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;
    private final LanguageRepository languageRepository;

    public ReferenceService(CountryRepository countryRepository, 
                          CurrencyRepository currencyRepository,
                          LanguageRepository languageRepository) {
        this.countryRepository = countryRepository;
        this.currencyRepository = currencyRepository;
        this.languageRepository = languageRepository;
    }

    public ReadableCountry[] listCountries() {
        return countryRepository.findAll().stream()
            .map(this::toReadableCountry)
            .toArray(ReadableCountry[]::new);
    }

    public ReadableCountry getCountry(String code) {
        return countryRepository.findByCode(code)
            .map(this::toReadableCountry)
            .orElse(null);
    }

    public ReadableCurrency[] listCurrencies() {
        return currencyRepository.findAll().stream()
            .map(this::toReadableCurrency)
            .toArray(ReadableCurrency[]::new);
    }

    public ReadableCurrency getCurrency(String code) {
        return currencyRepository.findByCode(code)
            .map(this::toReadableCurrency)
            .orElse(null);
    }

    public ReadableLanguage[] listLanguages() {
        return languageRepository.findAll().stream()
            .map(this::toReadableLanguage)
            .toArray(ReadableLanguage[]::new);
    }

    public ReadableLanguage getLanguage(String code) {
        return languageRepository.findByCode(code)
            .map(this::toReadableLanguage)
            .orElse(null);
    }

    public ReadableMeasure[] listMeasures() {
        ReadableMeasure kg = new ReadableMeasure();
        kg.setCode("kg");
        kg.setName("Kilogram");
        kg.setUnit("kg");
        return new ReadableMeasure[]{kg};
    }

    private ReadableCountry toReadableCountry(Country country) {
        ReadableCountry readable = new ReadableCountry();
        readable.setCode(country.getCode());
        readable.setName(country.getName());
        readable.setIsoCode(country.getIsoCode());
        return readable;
    }

    private ReadableCurrency toReadableCurrency(Currency currency) {
        ReadableCurrency readable = new ReadableCurrency();
        readable.setCode(currency.getCode());
        readable.setName(currency.getName());
        readable.setSymbol(currency.getSymbol());
        return readable;
    }

    private ReadableLanguage toReadableLanguage(Language language) {
        ReadableLanguage readable = new ReadableLanguage();
        readable.setCode(language.getCode());
        readable.setName(language.getName());
        return readable;
    }
}
