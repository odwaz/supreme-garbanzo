package com.spaza.product.controller;

import com.spaza.product.api.ProductVariantApiCtrl;
import com.spaza.product.entity.ProductVariant;
import com.spaza.product.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductVariantApiCtrlController implements ProductVariantApiCtrl {

    @Autowired
    private ProductVariantRepository variantRepository;

    @Override
    public ResponseEntity<Long> createVariant(Long productId, ProductVariant variant) {
        variant.setProductId(productId);
        ProductVariant saved = variantRepository.save(variant);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved.getId());
    }

    @Override
    public ResponseEntity<List<ProductVariant>> getVariants(Long productId) {
        return ResponseEntity.ok(variantRepository.findByProductId(productId));
    }

    @Override
    public ResponseEntity<Void> deleteVariant(Long productId, Long variantId) {
        variantRepository.deleteById(variantId);
        return ResponseEntity.noContent().build();
    }
}
