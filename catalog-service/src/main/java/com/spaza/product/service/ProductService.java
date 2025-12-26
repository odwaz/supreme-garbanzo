package com.spaza.product.service;

import com.spaza.product.entity.*;
import com.spaza.product.model.Product;
import com.spaza.product.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Long DEFAULT_LANGUAGE_ID = 1L;
    private final ProductRepository productRepository;
    private final ProductDescriptionRepository productDescriptionRepository;
    private final ProductAvailabilityRepository productAvailabilityRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantGroupRepository productVariantGroupRepository;
    private final ProductVariationRepository productVariationRepository;
    private final ProductVariantValueRepository productVariantValueRepository;
    private final com.spaza.content.repository.CatalogRepository catalogRepository;
    private final com.spaza.content.repository.CatalogEntryRepository catalogEntryRepository;
    private final com.spaza.category.repository.CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                         ProductDescriptionRepository productDescriptionRepository,
                         ProductAvailabilityRepository productAvailabilityRepository,
                         ProductPriceRepository productPriceRepository,
                         ProductVariantRepository productVariantRepository,
                         ProductVariantGroupRepository productVariantGroupRepository,
                         ProductVariationRepository productVariationRepository,
                         ProductVariantValueRepository productVariantValueRepository,
                         com.spaza.content.repository.CatalogRepository catalogRepository,
                         com.spaza.content.repository.CatalogEntryRepository catalogEntryRepository,
                         com.spaza.category.repository.CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productDescriptionRepository = productDescriptionRepository;
        this.productAvailabilityRepository = productAvailabilityRepository;
        this.productPriceRepository = productPriceRepository;
        this.productVariantRepository = productVariantRepository;
        this.productVariantGroupRepository = productVariantGroupRepository;
        this.productVariationRepository = productVariationRepository;
        this.productVariantValueRepository = productVariantValueRepository;
        this.catalogRepository = catalogRepository;
        this.catalogEntryRepository = catalogEntryRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Product save(Product product) {
        if (product.getPrice() == null || product.getPrice().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid product price");
        }
        if (product.getQuantity() == null || product.getQuantity() < 0) {
            throw new IllegalArgumentException("Invalid product quantity");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (product.getCategoryId() != null && !categoryRepository.existsById(product.getCategoryId())) {
            throw new IllegalArgumentException("Invalid category ID");
        }
        
        Product saved = productRepository.save(product);
        
        // Create ProductDescription
        if (saved.getName() != null) {
            ProductDescription desc = new ProductDescription();
            desc.setProductId(saved.getId());
            desc.setLanguageId(getDefaultLanguageId());
            desc.setName(saved.getName());
            desc.setDescription(saved.getDescription());
            productDescriptionRepository.save(desc);
        }
        
        // Create ProductAvailability
        ProductAvailability avail = new ProductAvailability();
        avail.setProductId(saved.getId());
        avail.setProductQuantity(saved.getQuantity());
        avail.setAvailable(saved.getAvailable());
        avail.setDateAvailable(LocalDateTime.now());
        ProductAvailability savedAvail = productAvailabilityRepository.save(avail);
        
        // Create ProductPrice
        ProductPrice price = new ProductPrice();
        price.setProductAvailId(savedAvail.getId());
        price.setProductPriceAmount(saved.getPrice());
        price.setDefaultPrice(true);
        productPriceRepository.save(price);
        
        syncToCatalog(saved);
        
        return saved;
    }
    
    private void syncToCatalog(Product product) {
        if (product.getMerchantId() == null) return;
        
        catalogRepository.findByMerchantIdAndDefaultCatalog(product.getMerchantId(), true)
            .ifPresent(catalog -> {
                catalogEntryRepository.findByCatalogIdAndProductId(catalog.getId(), product.getId())
                    .ifPresentOrElse(
                        entry -> {},
                        () -> {
                            com.spaza.content.model.CatalogEntry entry = new com.spaza.content.model.CatalogEntry();
                            entry.setCatalogId(catalog.getId());
                            entry.setProductId(product.getId());
                            entry.setCategoryId(product.getCategoryId());
                            entry.setVisible(product.getAvailable());
                            entry.setDateCreated(java.time.LocalDateTime.now());
                            catalogEntryRepository.save(entry);
                        }
                    );
            });
    }
    
    private Long getDefaultLanguageId() {
        return DEFAULT_LANGUAGE_ID;
    }

    @Transactional
    public void deleteById(Long id) {
        catalogEntryRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }

    public Optional<Product> findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        product.ifPresent(this::enrichProduct);
        return product;
    }

    public List<Product> findAll(int page, int count, Long merchant) {
        List<Product> products;
        
        if (merchant != null) {
            products = productRepository.findByMerchantId(merchant, PageRequest.of(page, count)).getContent();
        } else {
            products = productRepository.findAll(PageRequest.of(page, count)).getContent();
        }
        
        products.forEach(this::enrichProduct);
        return products;
    }
    
    private void enrichProduct(Product product) {
        // Fetch descriptions
        List<ProductDescription> descriptions = productDescriptionRepository.findByProductId(product.getId());
        if (!descriptions.isEmpty()) {
            product.setName(descriptions.get(0).getName());
            product.setDescription(descriptions.get(0).getDescription());
        }
        product.setDescriptions(descriptions);
        
        // Fetch availability and price
        List<ProductAvailability> availabilities = productAvailabilityRepository.findByProductId(product.getId());
        if (!availabilities.isEmpty()) {
            ProductAvailability avail = availabilities.get(0);
            product.setQuantity(avail.getProductQuantity());
            product.setAvailable(avail.getAvailable());
            
            // Fetch price from ProductPrice
            List<ProductPrice> prices = productPriceRepository.findByProductAvailId(avail.getId());
            if (!prices.isEmpty()) {
                product.setPrice(prices.get(0).getProductPriceAmount());
            }
        }
        product.setAvailabilities(availabilities);
        
        // Fetch variants
        product.setVariants(productVariantRepository.findByProductId(product.getId()));
        
        // Fetch variant groups with variations
        List<ProductVariantGroup> groups = productVariantGroupRepository.findByProductId(product.getId());
        for (ProductVariantGroup group : groups) {
            group.setVariations(productVariationRepository.findByVariantGroupId(group.getId()));
        }
        product.setVariantGroups(groups);
    }
}
