package com.spaza.content.repository;

import com.spaza.content.model.CatalogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatalogEntryRepository extends JpaRepository<CatalogEntry, Long> {
    List<CatalogEntry> findByCatalogId(Long catalogId);
    Optional<CatalogEntry> findByCatalogIdAndProductId(Long catalogId, Long productId);
    void deleteByProductId(Long productId);
}
