package com.spaza.category.repository;

import com.spaza.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByMerchantId(Long merchantId);
    List<Category> findByMerchantIdAndVisibleTrue(Long merchantId);
    List<Category> findByMerchantIdAndFeaturedTrueAndVisibleTrue(Long merchantId);
}
