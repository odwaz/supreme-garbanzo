package com.spaza.content.repository;

import com.spaza.content.model.ReadableContentPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentPageRepository extends JpaRepository<ReadableContentPage, Long> {
    Optional<ReadableContentPage> findByCode(String code);
    Optional<ReadableContentPage> findByName(String name);
}
