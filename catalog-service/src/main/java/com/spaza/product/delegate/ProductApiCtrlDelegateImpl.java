package com.spaza.product.delegate;

import com.spaza.product.model.*;
import com.spaza.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductApiCtrlDelegateImpl implements ProductApiCtrlDelegate {

    @Autowired
    private ProductService productService;

    @Override
    public ResponseEntity<Long> create(Product product) {
        Product saved = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved.getId());
    }

    @Override
    public ResponseEntity<Void> update(Long id, Product product) {
        product.setId(id);
        productService.save(product);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Product> getById(Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<Product>> list(String sku, String name, Boolean available, int page, int count, Long merchant) {
        List<Product> products = productService.findAll(sku, name, available, page, count, merchant);
        return ResponseEntity.ok(products);
    }
}
