package com.spaza.content.delegate;

import com.spaza.content.model.*;
import com.spaza.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ContentApiCtrlDelegateImpl implements ContentApiCtrlDelegate {

    @Autowired
    private ContentService contentService;

    @Override
    public ResponseEntity<Object[]> boxes(Integer count, Integer page) {
        Object[] boxes = contentService.getBoxes(count, page);
        return ResponseEntity.ok(boxes);
    }

    @Override
    public ResponseEntity<Object[]> getBoxByCode(String code) {
        Object[] box = contentService.getBoxByCode(code);
        return ResponseEntity.ok(box);
    }

    @Override
    public ResponseEntity<Object[]> pages(Integer count, Integer page) {
        Object[] pages = contentService.getPages(count, page);
        return ResponseEntity.ok(pages);
    }

    @Override
    public ResponseEntity<ReadableContentPage> page(String code) {
        ReadableContentPage page = contentService.getPage(code);
        return ResponseEntity.ok(page);
    }

    @Override
    public ResponseEntity<ReadableContentPage> pageByName(String name) {
        ReadableContentPage page = contentService.getPageByName(name);
        return ResponseEntity.ok(page);
    }

    @Override
    public ResponseEntity<ContentFolder> images(String path) {
        ContentFolder folder = contentService.getImages(path);
        return ResponseEntity.ok(folder);
    }

    @Override
    public ResponseEntity<Entity> createBox(PersistableContentBox box) {
        Entity entity = contentService.createBox(box);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @Override
    public ResponseEntity<Void> updateBox(Long id, PersistableContentBox box) {
        contentService.updateBox(id, box);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteBox(Long id) {
        contentService.deleteBox(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Entity> createPage(PersistableContentPage page) {
        Entity entity = contentService.createPage(page);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @Override
    public ResponseEntity<Void> updatePage(Long id, PersistableContentPage page) {
        contentService.updatePage(id, page);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deletePage(Long id) {
        contentService.deletePage(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> upload(MultipartFile file) {
        contentService.upload(file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}