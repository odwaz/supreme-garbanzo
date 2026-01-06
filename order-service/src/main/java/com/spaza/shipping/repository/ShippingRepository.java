package com.spaza.shipping.repository;

import com.spaza.shipping.model.ReadableShippingQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShippingRepository extends JpaRepository<ReadableShippingQuote, Long> {
    Optional<ReadableShippingQuote> findByCode(String code);
}