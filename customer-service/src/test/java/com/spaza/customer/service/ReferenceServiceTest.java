package com.spaza.customer.service;

import com.spaza.customer.model.*;
import com.spaza.customer.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReferenceServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private ReferenceService referenceService;

    private Country country;
    private Currency currency;
    private Language language;

    @BeforeEach
    void setUp() {
        country = new Country();
        country.setId(1L);
        country.setCode("ZA");
        country.setName("South Africa");
        country.setIsoCode("ZAF");

        currency = new Currency();
        currency.setId(1L);
        currency.setCode("ZAR");
        currency.setName("South African Rand");
        currency.setSymbol("R");

        language = new Language();
        language.setId(1L);
        language.setCode("en");
        language.setName("English");
    }

    @Test
    void listCountries_ShouldReturnAllCountries() {
        when(countryRepository.findAll()).thenReturn(Arrays.asList(country));

        ReadableCountry[] result = referenceService.listCountries();

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("ZA", result[0].getCode());
        assertEquals("South Africa", result[0].getName());
        verify(countryRepository).findAll();
    }

    @Test
    void getCountry_ShouldReturnCountry_WhenExists() {
        when(countryRepository.findByCode("ZA")).thenReturn(Optional.of(country));

        ReadableCountry result = referenceService.getCountry("ZA");

        assertNotNull(result);
        assertEquals("ZA", result.getCode());
        assertEquals("South Africa", result.getName());
        verify(countryRepository).findByCode("ZA");
    }

    @Test
    void getCountry_ShouldReturnNull_WhenNotExists() {
        when(countryRepository.findByCode("XX")).thenReturn(Optional.empty());

        ReadableCountry result = referenceService.getCountry("XX");

        assertNull(result);
        verify(countryRepository).findByCode("XX");
    }

    @Test
    void listCurrencies_ShouldReturnAllCurrencies() {
        when(currencyRepository.findAll()).thenReturn(Arrays.asList(currency));

        ReadableCurrency[] result = referenceService.listCurrencies();

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("ZAR", result[0].getCode());
        assertEquals("R", result[0].getSymbol());
        verify(currencyRepository).findAll();
    }

    @Test
    void getCurrency_ShouldReturnCurrency_WhenExists() {
        when(currencyRepository.findByCode("ZAR")).thenReturn(Optional.of(currency));

        ReadableCurrency result = referenceService.getCurrency("ZAR");

        assertNotNull(result);
        assertEquals("ZAR", result.getCode());
        assertEquals("South African Rand", result.getName());
        verify(currencyRepository).findByCode("ZAR");
    }

    @Test
    void listLanguages_ShouldReturnAllLanguages() {
        when(languageRepository.findAll()).thenReturn(Arrays.asList(language));

        ReadableLanguage[] result = referenceService.listLanguages();

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("en", result[0].getCode());
        assertEquals("English", result[0].getName());
        verify(languageRepository).findAll();
    }

    @Test
    void getLanguage_ShouldReturnLanguage_WhenExists() {
        when(languageRepository.findByCode("en")).thenReturn(Optional.of(language));

        ReadableLanguage result = referenceService.getLanguage("en");

        assertNotNull(result);
        assertEquals("en", result.getCode());
        assertEquals("English", result.getName());
        verify(languageRepository).findByCode("en");
    }

    @Test
    void listMeasures_ShouldReturnMeasures() {
        ReadableMeasure[] result = referenceService.listMeasures();

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("kg", result[0].getCode());
    }
}
