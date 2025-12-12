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

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductDescriptionRepository productDescriptionRepository;
    
    @Autowired
    private ProductAvailabilityRepository productAvailabilityRepository;
    
    @Autowired
    private ProductPriceRepository productPriceRepository;
    
    @Autowired
    private ProductVariantRepository productVariantRepository;
    
    @Autowired
    private ProductVariantGroupRepository productVariantGroupRepository;
    
    @Autowired
    private ProductVariationRepository productVariationRepository;
    
    @Autowired
    private ProductVariantValueRepository productVariantValueRepository;
    
    @Autowired
    private com.spaza.content.repository.CatalogRepository catalogRepository;
    
    @Autowired
    private com.spaza.content.repository.CatalogEntryRepository catalogEntryRepository;
    
    @Autowired
    private com.spaza.category.repository.CategoryRepository categoryRepository;

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
                    .orElseGet(() -> {
                        com.spaza.content.model.CatalogEntry entry = new com.spaza.content.model.CatalogEntry();
                        entry.setCatalogId(catalog.getId());
                        entry.setProductId(product.getId());
                        entry.setCategoryId(product.getCategoryId());
                        entry.setVisible(product.getAvailable());
                        entry.setDateCreated(java.time.LocalDateTime.now());
                        return catalogEntryRepository.save(entry);
                    });
            });
    }
    
    private Long getDefaultLanguageId() {
        return 1L; // English - should be fetched from language repository
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

    public List<Product> findAll(String sku, String name, Boolean available, int page, int count, Long merchant) {
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
