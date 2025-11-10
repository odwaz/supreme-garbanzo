package com.spaza.order.service;

import com.spaza.order.model.*;
import com.spaza.order.repository.*;
import org.springframework.stereotype.Service;

@Service
public class TaxService {

    private final TaxClassRepository taxClassRepository;
    private final TaxRateRepository taxRateRepository;

    public TaxService(TaxClassRepository taxClassRepository, TaxRateRepository taxRateRepository) {
        this.taxClassRepository = taxClassRepository;
        this.taxRateRepository = taxRateRepository;
    }

    public ReadableTaxClass[] listTaxClasses() {
        return taxClassRepository.findAll().stream()
            .map(this::toReadableTaxClass)
            .toArray(ReadableTaxClass[]::new);
    }

    public ReadableTaxClass createTaxClass(PersistableTaxClass taxClass) {
        TaxClass entity = new TaxClass();
        entity.setCode(taxClass.getCode());
        entity.setTitle(taxClass.getTitle());
        return toReadableTaxClass(taxClassRepository.save(entity));
    }

    public ReadableTaxRate[] listTaxRates() {
        return taxRateRepository.findAll().stream()
            .map(this::toReadableTaxRate)
            .toArray(ReadableTaxRate[]::new);
    }

    public ReadableTaxRate createTaxRate(PersistableTaxRate taxRate) {
        TaxRate entity = new TaxRate();
        entity.setCountry(taxRate.getCountry());
        entity.setRate(taxRate.getRate());
        entity.setTaxClassId(taxRate.getTaxClassId());
        return toReadableTaxRate(taxRateRepository.save(entity));
    }

    public void deleteTaxRate(Long id) {
        taxRateRepository.deleteById(id);
    }

    private ReadableTaxClass toReadableTaxClass(TaxClass entity) {
        ReadableTaxClass readable = new ReadableTaxClass();
        readable.setId(entity.getId());
        readable.setCode(entity.getCode());
        readable.setTitle(entity.getTitle());
        return readable;
    }

    private ReadableTaxRate toReadableTaxRate(TaxRate entity) {
        ReadableTaxRate readable = new ReadableTaxRate();
        readable.setId(entity.getId());
        readable.setCountry(entity.getCountry());
        readable.setRate(entity.getRate());
        readable.setTaxClassId(entity.getTaxClassId());
        return readable;
    }
}
