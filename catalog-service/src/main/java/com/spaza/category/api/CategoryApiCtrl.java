package com.spaza.category.api;

import com.spaza.category.model.Category;
import com.spaza.category.model.PersistableCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface CategoryApiCtrl {

    @PostMapping("/api/v1/private/category")
    ResponseEntity<Long> create(@RequestBody PersistableCategory category);

    @PutMapping("/api/v1/private/category/{id}")
    ResponseEntity<Void> update(@PathVariable Long id, @RequestBody PersistableCategory category);

    @DeleteMapping("/api/v1/private/category/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);

    @GetMapping("/api/v1/categories/{id}")
    ResponseEntity<Category> getById(@PathVariable Long id);

    @GetMapping("/api/v1/categories")
    ResponseEntity<List<Category>> listPublic(
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int count);

    @GetMapping("/api/v1/private/category")
    ResponseEntity<List<Category>> list(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int count);
}
