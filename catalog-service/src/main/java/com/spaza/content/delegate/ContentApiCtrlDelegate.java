package com.spaza.content.delegate;

import com.spaza.content.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ContentApiCtrlDelegate {
    ResponseEntity<Object[]> boxes(Integer count, Integer page);
    ResponseEntity<Object[]> getBoxByCode(String code);
    ResponseEntity<Object[]> pages(Integer count, Integer page);
    ResponseEntity<ReadableContentPage> page(String code);
    ResponseEntity<ReadableContentPage> pageByName(String name);
    ResponseEntity<ContentFolder> images(String path);
    ResponseEntity<Entity> createBox(PersistableContentBox box);
    ResponseEntity<Void> updateBox(Long id, PersistableContentBox box);
    ResponseEntity<Void> deleteBox(Long id);
    ResponseEntity<Entity> createPage(PersistableContentPage page);
    ResponseEntity<Void> updatePage(Long id, PersistableContentPage page);
    ResponseEntity<Void> deletePage(Long id);
    ResponseEntity<Void> upload(MultipartFile file);
}