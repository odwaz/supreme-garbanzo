package com.spaza.product.service;

import com.spaza.product.entity.ProductVariant;
import com.spaza.product.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductVariantService {
    
    @Autowired
    private ProductVariantRepository repository;
    
    public ProductVariant save(ProductVariant variant) {
        return repository.save(variant);
    }
    
    public Optional<ProductVariant> findById(Long id) {
        return repository.findById(id);
    }
    
    public List<ProductVariant> findByProductId(Long productId) {
        return repository.findByProductId(productId);
    }
    
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
