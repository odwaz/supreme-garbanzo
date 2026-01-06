package com.spaza.content.controller;

import com.spaza.content.api.CatalogApiCtrl;
import com.spaza.content.delegate.CatalogApiCtrlDelegate;
import com.spaza.content.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CatalogApiCtrlController implements CatalogApiCtrl {

    private final CatalogApiCtrlDelegate delegate;

    public CatalogApiCtrlController(CatalogApiCtrlDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public ResponseEntity<ReadableCatalog[]> list() {
        return delegate.list();
    }

    @Override
    public ResponseEntity<ReadableCatalog> get(Long id) {
        return delegate.get(id);
    }

    @Override
    public ResponseEntity<ReadableCatalog> create(PersistableCatalog catalog) {
        return delegate.create(catalog);
    }

    @Override
    public ResponseEntity<Void> update(Long id, PersistableCatalog catalog) {
        return delegate.update(id, catalog);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        return delegate.delete(id);
    }
}
