package com.spaza.customer.repository;

import com.spaza.customer.model.Customer;
import com.spaza.customer.model.ReadableCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);
    
    @Query("SELECT new com.spaza.customer.model.ReadableCustomer(c) FROM Customer c WHERE c.email = ?1")
    Optional<ReadableCustomer> findReadableByEmail(String email);
    
    @Query("SELECT new com.spaza.customer.model.ReadableCustomer(c) FROM Customer c WHERE c.id = ?1")
    Optional<ReadableCustomer> findReadableById(Long id);
    
    @Query("SELECT new com.spaza.customer.model.ReadableCustomer(c) FROM Customer c")
    List<ReadableCustomer> findAllReadable();
}