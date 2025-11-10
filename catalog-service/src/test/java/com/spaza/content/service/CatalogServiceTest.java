package com.spaza.content.service;

import com.spaza.content.model.*;
import com.spaza.content.repository.CatalogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogServiceTest {

    @Mock
    private CatalogRepository repository;

    @InjectMocks
    private CatalogService service;

    private Catalog catalog;
    private PersistableCatalog persistable;

    @BeforeEach
    void setUp() {
        catalog = new Catalog();
        catalog.setId(1L);
        catalog.setCode("CAT001");
        catalog.setName("Main Catalog");
        catalog.setVisible(true);

        persistable = new PersistableCatalog();
        persistable.setCode("CAT001");
        persistable.setName("Main Catalog");
        persistable.setVisible(true);
    }

    @Test
    void list_ShouldReturnAllCatalogs() {
        when(repository.findAll()).thenReturn(Arrays.asList(catalog));

        ReadableCatalog[] result = service.list();

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("CAT001", result[0].getCode());
        verify(repository).findAll();
    }

    @Test
    void get_ShouldReturnCatalog_WhenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(catalog));

        ReadableCatalog result = service.get(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Main Catalog", result.getName());
        verify(repository).findById(1L);
    }

    @Test
    void get_ShouldReturnNull_WhenNotExists() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        ReadableCatalog result = service.get(1L);

        assertNull(result);
        verify(repository).findById(1L);
    }

    @Test
    void create_ShouldSaveAndReturnCatalog() {
        when(repository.save(any(Catalog.class))).thenReturn(catalog);

        ReadableCatalog result = service.create(persistable);

        assertNotNull(result);
        assertEquals("CAT001", result.getCode());
        verify(repository).save(any(Catalog.class));
    }

    @Test
    void update_ShouldUpdateCatalog_WhenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(catalog));
        when(repository.save(any(Catalog.class))).thenReturn(catalog);

        service.update(1L, persistable);

        verify(repository).findById(1L);
        verify(repository).save(catalog);
    }

    @Test
    void delete_ShouldDeleteCatalog() {
        service.delete(1L);

        verify(repository).deleteById(1L);
    }
}
