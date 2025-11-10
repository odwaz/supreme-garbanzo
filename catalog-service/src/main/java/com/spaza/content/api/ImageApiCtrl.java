package com.spaza.content.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

public interface ImageApiCtrl {

    @PostMapping("/api/v1/private/content/images")
    ResponseEntity<String> upload(@RequestParam("file") MultipartFile file);

    @GetMapping("/api/v1/content/images/{name}")
    ResponseEntity<byte[]> get(@PathVariable String name);

    @DeleteMapping("/api/v1/private/content/images/{name}")
    ResponseEntity<Void> delete(@PathVariable String name);
}
