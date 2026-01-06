package com.spaza.product.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageApiCtrl {

    @PostMapping("/api/v1/private/product/{id}/image")
    ResponseEntity<Void> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") List<MultipartFile> file,
            @RequestParam(required = false) Boolean defaultImage,
            @RequestParam(required = false) Integer order);

    @GetMapping("/api/v1/product/{productId}/images")
    ResponseEntity<List<Object>> getImages(@PathVariable Long productId);

    @DeleteMapping("/api/v1/private/product/{id}/image/{imageId}")
    ResponseEntity<Void> deleteImage(@PathVariable Long id, @PathVariable Long imageId);

    @PatchMapping("/api/v1/private/product/{id}/image/{imageId}")
    ResponseEntity<Void> updateImageDetails(
            @PathVariable Long id,
            @PathVariable Long imageId,
            @RequestParam(required = false) Integer order);
}
