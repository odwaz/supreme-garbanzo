package com.spaza.product.controller;

import com.spaza.product.api.ProductPriceApiCtrl;
import com.spaza.product.entity.ProductAvailability;
import com.spaza.product.entity.ProductPrice;
import com.spaza.product.repository.ProductAvailabilityRepository;
import com.spaza.product.repository.ProductPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductPriceApiCtrlController implements ProductPriceApiCtrl {

    @Autowired
    private ProductPriceRepository priceRepository;

    @Autowired
    private ProductAvailabilityRepository availabilityRepository;

    @Override
    public ResponseEntity<Long> createPrice(Long productId, ProductPrice price) {
        List<ProductAvailability> availabilities = availabilityRepository.findByProductId(productId);
        if (availabilities.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        price.setProductAvailId(availabilities.get(0).getId());
        ProductPrice saved = priceRepository.save(price);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved.getId());
    }

    @Override
    public ResponseEntity<List<ProductPrice>> getPrices(Long productId) {
        List<ProductAvailability> availabilities = availabilityRepository.findByProductId(productId);
        List<ProductPrice> prices = new ArrayList<>();
        for (ProductAvailability avail : availabilities) {
            prices.addAll(priceRepository.findByProductAvailId(avail.getId()));
        }
        return ResponseEntity.ok(prices);
    }

    @Override
    public ResponseEntity<Void> updatePrice(Long productId, Long priceId, ProductPrice price) {
        price.setId(priceId);
        priceRepository.save(price);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deletePrice(Long productId, Long priceId) {
        priceRepository.deleteById(priceId);
        return ResponseEntity.noContent().build();
    }
}
