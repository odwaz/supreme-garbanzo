package com.spaza.product.api;

import com.spaza.product.entity.ProductVariant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ProductVariantApiCtrl {

    @PostMapping("/api/v1/private/product/{productId}/variant")
    ResponseEntity<Long> createVariant(@PathVariable Long productId, @RequestBody ProductVariant variant);

    @GetMapping("/api/v1/product/{productId}/variants")
    ResponseEntity<List<ProductVariant>> getVariants(@PathVariable Long productId);

    @DeleteMapping("/api/v1/private/product/{productId}/variant/{variantId}")
    ResponseEntity<Void> deleteVariant(@PathVariable Long productId, @PathVariable Long variantId);
}
