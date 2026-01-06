package com.spaza.payment.repository;

import com.spaza.payment.model.ReadableTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<ReadableTransaction, Long> {
    List<ReadableTransaction> findByOrderId(String orderId);
    List<ReadableTransaction> findByStatus(String status);
    List<ReadableTransaction> findByOrderIdAndStatus(String orderId, String status);
    boolean existsByOrderIdAndStatus(String orderId, String status);
}