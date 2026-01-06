package com.spaza.tax.repository;

import com.spaza.tax.entity.TaxClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaxClassRepository extends JpaRepository<TaxClass, Long> {
    Optional<TaxClass> findByCode(String code);
    List<TaxClass> findByMerchantId(Long merchantId);
}
