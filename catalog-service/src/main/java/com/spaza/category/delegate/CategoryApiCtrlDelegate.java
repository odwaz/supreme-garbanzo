package com.spaza.category.delegate;

import com.spaza.category.model.Category;
import com.spaza.category.model.PersistableCategory;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryApiCtrlDelegate {
    ResponseEntity<Long> create(PersistableCategory category);
    ResponseEntity<Void> update(Long id, PersistableCategory category);
    ResponseEntity<Void> delete(Long id);
    ResponseEntity<Category> getById(Long id);
    ResponseEntity<List<Category>> list(String name, int page, int count);
}
