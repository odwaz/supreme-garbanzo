package com.spaza.order.repository;

import com.spaza.order.model.ReadableOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<ReadableOrder, Long> {
    List<ReadableOrder> findByStatus(String status);
    List<ReadableOrder> findByCustomerId(Long customerId);
}