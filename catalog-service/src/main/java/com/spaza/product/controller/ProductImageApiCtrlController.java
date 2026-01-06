package com.spaza.product.controller;

import com.spaza.product.api.ProductImageApiCtrl;
import com.spaza.product.delegate.ProductImageApiCtrlDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ProductImageApiCtrlController implements ProductImageApiCtrl {

    private final ProductImageApiCtrlDelegate delegate;

    public ProductImageApiCtrlController(ProductImageApiCtrlDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public ResponseEntity<Void> uploadImage(Long id, List<MultipartFile> file, Boolean defaultImage, Integer order) {
        return delegate.uploadImage(id, file, defaultImage, order);
    }

    @Override
    public ResponseEntity<List<Object>> getImages(Long productId) {
        return delegate.getImages(productId);
    }

    @Override
    public ResponseEntity<Void> deleteImage(Long id, Long imageId) {
        return delegate.deleteImage(id, imageId);
    }

    @Override
    public ResponseEntity<Void> updateImageDetails(Long id, Long imageId, Integer order) {
        return delegate.updateImageDetails(id, imageId, order);
    }
}
