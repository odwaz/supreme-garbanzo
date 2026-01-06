package com.spaza.tax.service;

import com.spaza.tax.entity.TaxClass;
import com.spaza.tax.entity.TaxRate;
import com.spaza.tax.repository.TaxClassRepository;
import com.spaza.tax.repository.TaxRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaxService {
    
    @Autowired
    private TaxClassRepository taxClassRepository;
    
    @Autowired
    private TaxRateRepository taxRateRepository;
    
    public TaxClass saveTaxClass(TaxClass taxClass) {
        return taxClassRepository.save(taxClass);
    }
    
    public TaxRate saveTaxRate(TaxRate taxRate) {
        return taxRateRepository.save(taxRate);
    }
    
    public List<TaxClass> findTaxClassesByMerchantId(Long merchantId) {
        return taxClassRepository.findByMerchantId(merchantId);
    }
    
    public List<TaxRate> findTaxRatesByMerchantId(Long merchantId) {
        return taxRateRepository.findByMerchantId(merchantId);
    }
    
    public List<TaxRate> findTaxRatesByTaxClassId(Long taxClassId) {
        return taxRateRepository.findByTaxClassId(taxClassId);
    }
    
    public void deleteTaxClass(Long id) {
        taxClassRepository.deleteById(id);
    }
    
    public void deleteTaxRate(Long id) {
        taxRateRepository.deleteById(id);
    }
}
