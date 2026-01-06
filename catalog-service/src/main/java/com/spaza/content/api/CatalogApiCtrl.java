package com.spaza.content.api;

import com.spaza.content.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

public interface CatalogApiCtrl {

    @GetMapping("/api/v1/private/catalogs")
    ResponseEntity<ReadableCatalog[]> list();

    @GetMapping("/api/v1/private/catalog/{id}")
    ResponseEntity<ReadableCatalog> get(@PathVariable Long id);

    @PostMapping("/api/v1/private/catalog")
    ResponseEntity<ReadableCatalog> create(@Valid @RequestBody PersistableCatalog catalog);

    @PatchMapping("/api/v1/private/catalog/{id}")
    ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody PersistableCatalog catalog);

    @DeleteMapping("/api/v1/private/catalog/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}
