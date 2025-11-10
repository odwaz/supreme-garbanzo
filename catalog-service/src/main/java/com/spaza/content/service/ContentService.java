package com.spaza.content.service;

import com.spaza.content.model.*;
import com.spaza.content.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ContentService {

    @Autowired
    private ContentBoxRepository boxRepository;
    
    @Autowired
    private ContentPageRepository pageRepository;

    public Object[] getBoxes(Integer count, Integer page) {
        return boxRepository.findAll().toArray();
    }

    public Object[] getBoxByCode(String code) {
        return boxRepository.findByCode(code)
                .map(box -> new Object[]{box})
                .orElse(new Object[0]);
    }

    public Object[] getPages(Integer count, Integer page) {
        return pageRepository.findAll().toArray();
    }

    public ReadableContentPage getPage(String code) {
        return pageRepository.findByCode(code).orElseGet(() -> {
            ReadableContentPage page = new ReadableContentPage();
            page.setCode(code);
            page.setName("Sample Page");
            return page;
        });
    }

    public ReadableContentPage getPageByName(String name) {
        return pageRepository.findByName(name).orElseGet(() -> {
            ReadableContentPage page = new ReadableContentPage();
            page.setName(name);
            return page;
        });
    }

    public ContentFolder getImages(String path) {
        ContentFolder folder = new ContentFolder();
        folder.setPath(path != null ? path : "/");
        return folder;
    }

    public Entity createBox(PersistableContentBox box) {
        ReadableContentBox readableBox = new ReadableContentBox();
        readableBox.setCode(box.getCode());
        readableBox.setName(box.getName());
        ReadableContentBox saved = boxRepository.save(readableBox);
        Entity entity = new Entity();
        entity.setId(saved.getId());
        entity.setCode(saved.getCode());
        return entity;
    }

    public void updateBox(Long id, PersistableContentBox box) {
        boxRepository.findById(id).ifPresent(readableBox -> {
            readableBox.setCode(box.getCode());
            readableBox.setName(box.getName());
            boxRepository.save(readableBox);
        });
    }

    public void deleteBox(Long id) {
        boxRepository.deleteById(id);
    }

    public Entity createPage(PersistableContentPage page) {
        ReadableContentPage readablePage = new ReadableContentPage();
        readablePage.setCode(page.getCode());
        readablePage.setName(page.getName());
        ReadableContentPage saved = pageRepository.save(readablePage);
        Entity entity = new Entity();
        entity.setId(saved.getId());
        entity.setCode(saved.getCode());
        return entity;
    }

    public void updatePage(Long id, PersistableContentPage page) {
        pageRepository.findById(id).ifPresent(readablePage -> {
            readablePage.setCode(page.getCode());
            readablePage.setName(page.getName());
            pageRepository.save(readablePage);
        });
    }

    public void deletePage(Long id) {
        pageRepository.deleteById(id);
    }

    public void upload(MultipartFile file) {
        // File upload logic
    }
}