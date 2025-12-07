package com.spaza.product.api;

import com.spaza.product.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ProductApiCtrl {

    @PostMapping("/api/v1/private/product")
    ResponseEntity<Long> create(@RequestBody Product product);

    @PutMapping("/api/v1/private/product/{id}")
    ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Product product);

    @DeleteMapping("/api/v1/private/product/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);

    @GetMapping("/api/v1/product/{id}")
    ResponseEntity<Product> getById(@PathVariable Long id);

    @GetMapping("/api/v1/products")
    ResponseEntity<List<Product>> list(
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean available,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int count,
            @RequestParam(required = false) Long merchant);
}
