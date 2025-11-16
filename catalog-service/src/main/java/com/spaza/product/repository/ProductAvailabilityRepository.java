package com.spaza.product.repository;

import com.spaza.product.entity.ProductAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductAvailabilityRepository extends JpaRepository<ProductAvailability, Long> {
    List<ProductAvailability> findByProductId(Long productId);
}
