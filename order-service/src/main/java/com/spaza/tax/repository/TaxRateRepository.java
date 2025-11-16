package com.spaza.tax.repository;

import com.spaza.tax.entity.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaxRateRepository extends JpaRepository<TaxRate, Long> {
    List<TaxRate> findByMerchantId(Long merchantId);
    List<TaxRate> findByTaxClassId(Long taxClassId);
}
