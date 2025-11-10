package com.spaza.cart.repository;

import com.spaza.cart.model.ReadableShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<ReadableShoppingCart, Long> {
    Optional<ReadableShoppingCart> findByCode(String code);
}