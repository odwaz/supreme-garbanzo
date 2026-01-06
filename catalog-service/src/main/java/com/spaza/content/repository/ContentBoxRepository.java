package com.spaza.content.repository;

import com.spaza.content.model.ReadableContentBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentBoxRepository extends JpaRepository<ReadableContentBox, Long> {
    Optional<ReadableContentBox> findByCode(String code);
}
