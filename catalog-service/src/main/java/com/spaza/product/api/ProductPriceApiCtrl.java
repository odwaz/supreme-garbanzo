package com.spaza.product.api;

import com.spaza.product.entity.ProductPrice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ProductPriceApiCtrl {

    @PostMapping("/api/v1/private/product/{productId}/price")
    ResponseEntity<Long> createPrice(@PathVariable Long productId, @RequestBody ProductPrice price);

    @GetMapping("/api/v1/product/{productId}/prices")
    ResponseEntity<List<ProductPrice>> getPrices(@PathVariable Long productId);

    @PutMapping("/api/v1/private/product/{productId}/price/{priceId}")
    ResponseEntity<Void> updatePrice(@PathVariable Long productId, @PathVariable Long priceId, @RequestBody ProductPrice price);

    @DeleteMapping("/api/v1/private/product/{productId}/price/{priceId}")
    ResponseEntity<Void> deletePrice(@PathVariable Long productId, @PathVariable Long priceId);
}
