package com.spaza.content.delegate;

import com.spaza.content.model.*;
import com.spaza.content.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatalogApiCtrlDelegateImpl implements CatalogApiCtrlDelegate {

    private final CatalogService catalogService;

    @Override
    public ResponseEntity<ReadableCatalog[]> list() {
        return ResponseEntity.ok(catalogService.list());
    }

    @Override
    public ResponseEntity<ReadableCatalog> get(Long id) {
        return ResponseEntity.ok(catalogService.get(id));
    }

    @Override
    public ResponseEntity<ReadableCatalog> create(PersistableCatalog catalog) {
        return ResponseEntity.ok(catalogService.create(catalog));
    }

    @Override
    public ResponseEntity<Void> update(Long id, PersistableCatalog catalog) {
        catalogService.update(id, catalog);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        catalogService.delete(id);
        return ResponseEntity.ok().build();
    }
}
