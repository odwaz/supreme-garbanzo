package com.spaza.product.repository;

import com.spaza.product.entity.ProductVariantValue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductVariantValueRepository extends JpaRepository<ProductVariantValue, Long> {
    List<ProductVariantValue> findByProductVariantId(Long productVariantId);
}
