package com.spaza.content.service;

import com.spaza.content.model.*;
import com.spaza.content.repository.CatalogRepository;
import org.springframework.stereotype.Service;

@Service
public class CatalogService {

    private final CatalogRepository repository;

    public CatalogService(CatalogRepository repository) {
        this.repository = repository;
    }

    public ReadableCatalog[] list() {
        return repository.findAll().stream()
            .map(this::toReadable)
            .toArray(ReadableCatalog[]::new);
    }

    public ReadableCatalog get(Long id) {
        return repository.findById(id)
            .map(this::toReadable)
            .orElse(null);
    }

    public ReadableCatalog create(PersistableCatalog catalog) {
        Catalog entity = new Catalog();
        entity.setCode(catalog.getCode());
        entity.setName(catalog.getName());
        entity.setVisible(catalog.getVisible());
        return toReadable(repository.save(entity));
    }

    public void update(Long id, PersistableCatalog catalog) {
        repository.findById(id).ifPresent(entity -> {
            entity.setCode(catalog.getCode());
            entity.setName(catalog.getName());
            entity.setVisible(catalog.getVisible());
            repository.save(entity);
        });
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private ReadableCatalog toReadable(Catalog entity) {
        ReadableCatalog readable = new ReadableCatalog();
        readable.setId(entity.getId());
        readable.setCode(entity.getCode());
        readable.setName(entity.getName());
        readable.setVisible(entity.getVisible());
        return readable;
    }
}
