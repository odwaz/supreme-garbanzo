package com.spaza.content.api;

import com.spaza.content.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

public interface ContentApiCtrl {

    @GetMapping("/api/v1/content/boxes")
    ResponseEntity<Object[]> boxes(@RequestParam(required = false) Integer count, @RequestParam(required = false) Integer page);

    @GetMapping("/api/v1/content/boxes/{code}")
    ResponseEntity<Object[]> getBoxByCode(@PathVariable String code);

    @GetMapping("/api/v1/content/pages")
    ResponseEntity<Object[]> pages(@RequestParam(required = false) Integer count, @RequestParam(required = false) Integer page);

    @GetMapping("/api/v1/content/pages/{code}")
    ResponseEntity<ReadableContentPage> page(@PathVariable String code);

    @GetMapping("/api/v1/content/pages/name/{name}")
    ResponseEntity<ReadableContentPage> pageByName(@PathVariable String name);

    @GetMapping("/api/v1/content/images")
    ResponseEntity<ContentFolder> images(@RequestParam(required = false) String path);

    @PostMapping("/api/v1/private/content/box")
    ResponseEntity<Entity> createBox(@Valid @RequestBody PersistableContentBox box);

    @PutMapping("/api/v1/private/content/box/{id}")
    ResponseEntity<Void> updateBox(@PathVariable Long id, @Valid @RequestBody PersistableContentBox box);

    @DeleteMapping("/api/v1/private/content/box/{id}")
    ResponseEntity<Void> deleteBox(@PathVariable Long id);

    @PostMapping("/api/v1/private/content/page")
    ResponseEntity<Entity> createPage(@Valid @RequestBody PersistableContentPage page);

    @PutMapping("/api/v1/private/content/page/{id}")
    ResponseEntity<Void> updatePage(@PathVariable Long id, @Valid @RequestBody PersistableContentPage page);

    @DeleteMapping("/api/v1/private/content/page/{id}")
    ResponseEntity<Void> deletePage(@PathVariable Long id);

    @PostMapping("/api/v1/private/file")
    ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file);
}