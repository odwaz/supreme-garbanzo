package com.spaza.content.repository;

import com.spaza.content.model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    java.util.Optional<Catalog> findByMerchantIdAndDefaultCatalog(Long merchantId, Boolean defaultCatalog);
}
