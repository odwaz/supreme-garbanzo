package com.spaza.product.delegate;

import com.spaza.product.model.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductApiCtrlDelegate {
    ResponseEntity<Long> create(Product product);
    ResponseEntity<Void> update(Long id, Product product);
    ResponseEntity<Void> delete(Long id);
    ResponseEntity<Product> getById(Long id);
    ResponseEntity<List<Product>> list(String sku, String name, Boolean available, int page, int count);
}
