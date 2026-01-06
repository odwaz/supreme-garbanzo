package com.spaza.content.controller;

import com.spaza.content.api.ContentApiCtrl;
import com.spaza.content.delegate.ContentApiCtrlDelegate;
import com.spaza.content.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
public class ContentApiCtrlController implements ContentApiCtrl {

    private final ContentApiCtrlDelegate delegate;

    public ContentApiCtrlController(ContentApiCtrlDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public ResponseEntity<Object[]> boxes(@RequestParam(required = false) Integer count, @RequestParam(required = false) Integer page) {
        return delegate.boxes(count, page);
    }

    @Override
    public ResponseEntity<Object[]> getBoxByCode(@PathVariable String code) {
        return delegate.getBoxByCode(code);
    }

    @Override
    public ResponseEntity<Object[]> pages(@RequestParam(required = false) Integer count, @RequestParam(required = false) Integer page) {
        return delegate.pages(count, page);
    }

    @Override
    public ResponseEntity<ReadableContentPage> page(@PathVariable String code) {
        return delegate.page(code);
    }

    @Override
    public ResponseEntity<ReadableContentPage> pageByName(@PathVariable String name) {
        return delegate.pageByName(name);
    }

    @Override
    public ResponseEntity<ContentFolder> images(@RequestParam(required = false) String path) {
        return delegate.images(path);
    }

    @Override
    public ResponseEntity<Entity> createBox(@Valid @RequestBody PersistableContentBox box) {
        return delegate.createBox(box);
    }

    @Override
    public ResponseEntity<Void> updateBox(@PathVariable Long id, @Valid @RequestBody PersistableContentBox box) {
        return delegate.updateBox(id, box);
    }

    @Override
    public ResponseEntity<Void> deleteBox(@PathVariable Long id) {
        return delegate.deleteBox(id);
    }

    @Override
    public ResponseEntity<Entity> createPage(@Valid @RequestBody PersistableContentPage page) {
        return delegate.createPage(page);
    }

    @Override
    public ResponseEntity<Void> updatePage(@PathVariable Long id, @Valid @RequestBody PersistableContentPage page) {
        return delegate.updatePage(id, page);
    }

    @Override
    public ResponseEntity<Void> deletePage(@PathVariable Long id) {
        return delegate.deletePage(id);
    }

    @Override
    public ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file) {
        return delegate.upload(file);
    }
}