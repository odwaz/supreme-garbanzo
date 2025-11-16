package com.spaza.category.delegate;

import com.spaza.category.model.Category;
import com.spaza.category.model.PersistableCategory;
import com.spaza.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryApiCtrlDelegateImpl implements CategoryApiCtrlDelegate {

    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseEntity<Long> create(PersistableCategory category) {
        Category entity = toEntity(category);
        Category saved = categoryService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved.getId());
    }

    @Override
    public ResponseEntity<Void> update(Long id, PersistableCategory category) {
        Category entity = toEntity(category);
        entity.setId(id);
        categoryService.save(entity);
        return ResponseEntity.ok().build();
    }

    private Category toEntity(PersistableCategory dto) {
        Category entity = new Category();
        entity.setCode(dto.getCode());
        entity.setVisible(dto.getVisible());
        entity.setFeatured(dto.getFeatured());
        entity.setSortOrder(dto.getSortOrder());
        entity.setMerchantId(dto.getMerchantId());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Category> getById(Long id) {
        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<Category>> list(String name, int page, int count) {
        List<Category> categories = categoryService.findAll(name, page, count);
        return ResponseEntity.ok(categories);
    }
}
