package com.spaza.category.controller;

import com.spaza.category.api.CategoryApiCtrl;
import com.spaza.category.delegate.CategoryApiCtrlDelegate;
import com.spaza.category.model.Category;
import com.spaza.category.model.PersistableCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryApiCtrlController implements CategoryApiCtrl {

    @Autowired
    private CategoryApiCtrlDelegate delegate;

    @Override
    public ResponseEntity<Long> create(PersistableCategory category) {
        return delegate.create(category);
    }

    @Override
    public ResponseEntity<Void> update(Long id, PersistableCategory category) {
        return delegate.update(id, category);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        return delegate.delete(id);
    }

    @Override
    public ResponseEntity<Category> getById(Long id) {
        return delegate.getById(id);
    }

    @Override
    public ResponseEntity<List<Category>> listPublic(String filter, int page, int count) {
        return delegate.listPublic(filter, page, count);
    }

    @Override
    public ResponseEntity<List<Category>> list(String name, int page, int count) {
        return delegate.list(name, page, count);
    }
}
