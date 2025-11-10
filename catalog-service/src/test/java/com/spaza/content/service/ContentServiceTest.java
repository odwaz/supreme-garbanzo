package com.spaza.content.service;

import com.spaza.content.model.*;
import com.spaza.content.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContentServiceTest {

    @Mock
    private ContentBoxRepository boxRepository;
    
    @Mock
    private ContentPageRepository pageRepository;
    
    @InjectMocks
    private ContentService contentService;
    
    private PersistableContentBox contentBox;
    private PersistableContentPage contentPage;

    @BeforeEach
    void setUp() {
        
        contentBox = new PersistableContentBox();
        contentBox.setCode("BANNER");
        contentBox.setName("Main Banner");

        contentPage = new PersistableContentPage();
        contentPage.setCode("ABOUT");
        contentPage.setName("About Us");
    }

    @Test
    void getBoxes_ShouldReturnBoxArray() {
        when(boxRepository.findAll()).thenReturn(Arrays.asList());
        Object[] result = contentService.getBoxes(10, 0);
        assertNotNull(result);
    }

    @Test
    void getBoxByCode_ShouldReturnSpecificBox() {
        when(boxRepository.findByCode("BANNER")).thenReturn(Optional.empty());
        Object[] result = contentService.getBoxByCode("BANNER");
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void getPages_ShouldReturnPageArray() {
        when(pageRepository.findAll()).thenReturn(Arrays.asList());
        Object[] result = contentService.getPages(10, 0);
        assertNotNull(result);
    }

    @Test
    void getPage_ShouldReturnReadablePage() {
        when(pageRepository.findByCode("ABOUT")).thenReturn(Optional.empty());
        ReadableContentPage result = contentService.getPage("ABOUT");
        assertNotNull(result);
        assertEquals("ABOUT", result.getCode());
    }

    @Test
    void getPageByName_ShouldReturnReadablePage() {
        when(pageRepository.findByName("About Us")).thenReturn(Optional.empty());
        ReadableContentPage result = contentService.getPageByName("About Us");
        assertNotNull(result);
        assertEquals("About Us", result.getName());
    }

    @Test
    void getImages_ShouldReturnContentFolder() {
        ContentFolder result = contentService.getImages("/uploads");
        assertNotNull(result);
        assertEquals("/uploads", result.getPath());
    }

    @Test
    void getImages_ShouldReturnRootFolder_WhenPathNull() {
        ContentFolder result = contentService.getImages(null);
        assertNotNull(result);
        assertEquals("/", result.getPath());
    }

    @Test
    void createBox_ShouldSaveAndReturnEntity() {
        ReadableContentBox mockBox = new ReadableContentBox();
        mockBox.setId(1L);
        mockBox.setCode("BANNER");
        when(boxRepository.save(any(ReadableContentBox.class))).thenReturn(mockBox);
        
        Entity result = contentService.createBox(contentBox);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("BANNER", result.getCode());
        verify(boxRepository).save(any(ReadableContentBox.class));
    }

    @Test
    void updateBox_ShouldSaveBox() {
        ReadableContentBox existingBox = new ReadableContentBox();
        existingBox.setId(1L);
        when(boxRepository.findById(1L)).thenReturn(Optional.of(existingBox));
        when(boxRepository.save(any(ReadableContentBox.class))).thenReturn(existingBox);
        
        assertDoesNotThrow(() -> contentService.updateBox(1L, contentBox));
        verify(boxRepository).findById(1L);
        verify(boxRepository).save(any(ReadableContentBox.class));
    }

    @Test
    void deleteBox_ShouldDeleteById() {
        assertDoesNotThrow(() -> contentService.deleteBox(1L));
        verify(boxRepository).deleteById(1L);
    }

    @Test
    void createPage_ShouldSaveAndReturnEntity() {
        ReadableContentPage mockPage = new ReadableContentPage();
        mockPage.setId(1L);
        mockPage.setCode("ABOUT");
        when(pageRepository.save(any(ReadableContentPage.class))).thenReturn(mockPage);
        
        Entity result = contentService.createPage(contentPage);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ABOUT", result.getCode());
        verify(pageRepository).save(any(ReadableContentPage.class));
    }

    @Test
    void updatePage_ShouldSavePage() {
        ReadableContentPage existingPage = new ReadableContentPage();
        existingPage.setId(1L);
        when(pageRepository.findById(1L)).thenReturn(Optional.of(existingPage));
        when(pageRepository.save(any(ReadableContentPage.class))).thenReturn(existingPage);
        
        assertDoesNotThrow(() -> contentService.updatePage(1L, contentPage));
        verify(pageRepository).findById(1L);
        verify(pageRepository).save(any(ReadableContentPage.class));
    }

    @Test
    void deletePage_ShouldDeleteById() {
        assertDoesNotThrow(() -> contentService.deletePage(1L));
        verify(pageRepository).deleteById(1L);
    }

    @Test
    void upload_ShouldExecuteWithoutError() {
        MultipartFile file = mock(MultipartFile.class);
        assertDoesNotThrow(() -> contentService.upload(file));
    }
}