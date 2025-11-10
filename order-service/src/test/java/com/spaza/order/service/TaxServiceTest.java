package com.spaza.order.service;

import com.spaza.order.model.*;
import com.spaza.order.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxServiceTest {

    @Mock
    private TaxClassRepository taxClassRepository;

    @Mock
    private TaxRateRepository taxRateRepository;

    @InjectMocks
    private TaxService service;

    private TaxClass taxClass;
    private TaxRate taxRate;
    private PersistableTaxClass persistableTaxClass;
    private PersistableTaxRate persistableTaxRate;

    @BeforeEach
    void setUp() {
        taxClass = new TaxClass();
        taxClass.setId(1L);
        taxClass.setCode("VAT");
        taxClass.setTitle("Value Added Tax");

        taxRate = new TaxRate();
        taxRate.setId(1L);
        taxRate.setCountry("ZA");
        taxRate.setRate(new BigDecimal("0.15"));
        taxRate.setTaxClassId(1L);

        persistableTaxClass = new PersistableTaxClass();
        persistableTaxClass.setCode("VAT");
        persistableTaxClass.setTitle("Value Added Tax");

        persistableTaxRate = new PersistableTaxRate();
        persistableTaxRate.setCountry("ZA");
        persistableTaxRate.setRate(new BigDecimal("0.15"));
        persistableTaxRate.setTaxClassId(1L);
    }

    @Test
    void listTaxClasses_ShouldReturnAllTaxClasses() {
        when(taxClassRepository.findAll()).thenReturn(Arrays.asList(taxClass));

        ReadableTaxClass[] result = service.listTaxClasses();

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("VAT", result[0].getCode());
        verify(taxClassRepository).findAll();
    }

    @Test
    void createTaxClass_ShouldSaveAndReturnTaxClass() {
        when(taxClassRepository.save(any(TaxClass.class))).thenReturn(taxClass);

        ReadableTaxClass result = service.createTaxClass(persistableTaxClass);

        assertNotNull(result);
        assertEquals("VAT", result.getCode());
        verify(taxClassRepository).save(any(TaxClass.class));
    }

    @Test
    void listTaxRates_ShouldReturnAllTaxRates() {
        when(taxRateRepository.findAll()).thenReturn(Arrays.asList(taxRate));

        ReadableTaxRate[] result = service.listTaxRates();

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("ZA", result[0].getCountry());
        assertEquals(new BigDecimal("0.15"), result[0].getRate());
        verify(taxRateRepository).findAll();
    }

    @Test
    void createTaxRate_ShouldSaveAndReturnTaxRate() {
        when(taxRateRepository.save(any(TaxRate.class))).thenReturn(taxRate);

        ReadableTaxRate result = service.createTaxRate(persistableTaxRate);

        assertNotNull(result);
        assertEquals("ZA", result.getCountry());
        verify(taxRateRepository).save(any(TaxRate.class));
    }

    @Test
    void deleteTaxRate_ShouldDeleteTaxRate() {
        service.deleteTaxRate(1L);

        verify(taxRateRepository).deleteById(1L);
    }
}
