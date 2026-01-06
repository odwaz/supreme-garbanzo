package com.spaza.product.repository;

import com.spaza.product.entity.ProductVariantGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductVariantGroupRepository extends JpaRepository<ProductVariantGroup, Long> {
    List<ProductVariantGroup> findByProductId(Long productId);
}
