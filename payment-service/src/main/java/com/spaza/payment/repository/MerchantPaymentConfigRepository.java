package com.spaza.payment.repository;

import com.spaza.payment.model.MerchantPaymentConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantPaymentConfigRepository extends JpaRepository<MerchantPaymentConfig, Long> {
    Optional<MerchantPaymentConfig> findByMerchantIdAndPaymentMethod(Long merchantId, String paymentMethod);
    List<MerchantPaymentConfig> findByMerchantId(Long merchantId);
    List<MerchantPaymentConfig> findByMerchantIdAndEnabled(Long merchantId, Boolean enabled);
}
