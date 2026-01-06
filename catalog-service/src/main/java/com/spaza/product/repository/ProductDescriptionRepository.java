package com.spaza.product.repository;

import com.spaza.product.entity.ProductDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductDescriptionRepository extends JpaRepository<ProductDescription, Long> {
    List<ProductDescription> findByProductId(Long productId);
}
