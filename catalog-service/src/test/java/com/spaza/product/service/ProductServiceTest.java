package com.spaza.product.service;

import com.spaza.category.repository.CategoryRepository;
import com.spaza.content.model.Catalog;
import com.spaza.content.model.CatalogEntry;
import com.spaza.content.repository.CatalogEntryRepository;
import com.spaza.content.repository.CatalogRepository;
import com.spaza.product.entity.*;
import com.spaza.product.model.Product;
import com.spaza.product.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private ProductDescriptionRepository productDescriptionRepository;
    @Mock private ProductAvailabilityRepository productAvailabilityRepository;
    @Mock private ProductPriceRepository productPriceRepository;
    @Mock private CatalogRepository catalogRepository;
    @Mock private CatalogEntryRepository catalogEntryRepository;
    @Mock private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Catalog catalog;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100));
        product.setQuantity(10);
        product.setMerchantId(1L);
        product.setCategoryId(1L);
        product.setAvailable(true);

        catalog = new Catalog();
        catalog.setId(1L);
        catalog.setMerchantId(1L);
        catalog.setDefaultCatalog(true);
    }

    @Test
    void save_shouldValidatePrice() {
        product.setPrice(BigDecimal.valueOf(-10));
        
        assertThrows(IllegalArgumentException.class, () -> productService.save(product));
    }

    @Test
    void save_shouldValidateQuantity() {
        product.setQuantity(-5);
        
        assertThrows(IllegalArgumentException.class, () -> productService.save(product));
    }

    @Test
    void save_shouldValidateName() {
        product.setName("");
        
        assertThrows(IllegalArgumentException.class, () -> productService.save(product));
    }

    @Test
    void save_shouldValidateCategoryExists() {
        when(categoryRepository.existsById(1L)).thenReturn(false);
        
        assertThrows(IllegalArgumentException.class, () -> productService.save(product));
    }

    @Test
    void save_shouldSyncToDefaultCatalog() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(productRepository.save(any())).thenReturn(product);
        when(productDescriptionRepository.save(any())).thenReturn(new ProductDescription());
        when(productAvailabilityRepository.save(any())).thenReturn(new ProductAvailability());
        when(productPriceRepository.save(any())).thenReturn(new ProductPrice());
        when(catalogRepository.findByMerchantIdAndDefaultCatalog(1L, true)).thenReturn(Optional.of(catalog));
        when(catalogEntryRepository.findByCatalogIdAndProductId(1L, 1L)).thenReturn(Optional.empty());
        when(catalogEntryRepository.save(any())).thenReturn(new CatalogEntry());

        productService.save(product);

        verify(catalogEntryRepository).save(any(CatalogEntry.class));
    }

    @Test
    void save_shouldNotDuplicateCatalogEntry() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(productRepository.save(any())).thenReturn(product);
        when(productDescriptionRepository.save(any())).thenReturn(new ProductDescription());
        when(productAvailabilityRepository.save(any())).thenReturn(new ProductAvailability());
        when(productPriceRepository.save(any())).thenReturn(new ProductPrice());
        when(catalogRepository.findByMerchantIdAndDefaultCatalog(1L, true)).thenReturn(Optional.of(catalog));
        when(catalogEntryRepository.findByCatalogIdAndProductId(1L, 1L)).thenReturn(Optional.of(new CatalogEntry()));

        productService.save(product);

        verify(catalogEntryRepository, never()).save(any());
    }

    @Test
    void save_shouldNotSyncIfNoDefaultCatalog() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(productRepository.save(any())).thenReturn(product);
        when(productDescriptionRepository.save(any())).thenReturn(new ProductDescription());
        when(productAvailabilityRepository.save(any())).thenReturn(new ProductAvailability());
        when(productPriceRepository.save(any())).thenReturn(new ProductPrice());
        when(catalogRepository.findByMerchantIdAndDefaultCatalog(1L, true)).thenReturn(Optional.empty());

        productService.save(product);

        verify(catalogEntryRepository, never()).save(any());
    }

    @Test
    void deleteById_shouldRemoveCatalogEntries() {
        productService.deleteById(1L);

        verify(catalogEntryRepository).deleteByProductId(1L);
        verify(productRepository).deleteById(1L);
    }
}
