package com.spaza.product.controller;

import com.spaza.product.api.ProductApiCtrl;
import com.spaza.product.delegate.ProductApiCtrlDelegate;
import com.spaza.product.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductApiCtrlController implements ProductApiCtrl {

    private final ProductApiCtrlDelegate delegate;

    public ProductApiCtrlController(ProductApiCtrlDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public ResponseEntity<Long> create(Product product) {
        return delegate.create(product);
    }

    @Override
    public ResponseEntity<Void> update(Long id, Product product) {
        return delegate.update(id, product);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        return delegate.delete(id);
    }

    @Override
    public ResponseEntity<Product> getById(Long id) {
        return delegate.getById(id);
    }

    @Override
    public ResponseEntity<List<Product>> list(String sku, String name, Boolean available, int page, int count, Long merchant) {
        return delegate.list(sku, name, available, page, count, merchant);
    }
}
