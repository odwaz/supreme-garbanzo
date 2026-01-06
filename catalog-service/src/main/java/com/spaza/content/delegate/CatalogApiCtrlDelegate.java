package com.spaza.content.delegate;

import com.spaza.content.model.*;
import org.springframework.http.ResponseEntity;

public interface CatalogApiCtrlDelegate {
    ResponseEntity<ReadableCatalog[]> list();
    ResponseEntity<ReadableCatalog> get(Long id);
    ResponseEntity<ReadableCatalog> create(PersistableCatalog catalog);
    ResponseEntity<Void> update(Long id, PersistableCatalog catalog);
    ResponseEntity<Void> delete(Long id);
}
