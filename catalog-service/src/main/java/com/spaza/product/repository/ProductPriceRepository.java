package com.spaza.product.repository;

import com.spaza.product.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    List<ProductPrice> findByProductAvailId(Long productAvailId);
}
