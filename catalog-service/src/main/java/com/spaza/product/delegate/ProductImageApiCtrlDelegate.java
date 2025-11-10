package com.spaza.product.delegate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageApiCtrlDelegate {
    ResponseEntity<Void> uploadImage(Long id, List<MultipartFile> file, Boolean defaultImage, Integer order);
    ResponseEntity<List<Object>> getImages(Long productId);
    ResponseEntity<Void> deleteImage(Long id, Long imageId);
    ResponseEntity<Void> updateImageDetails(Long id, Long imageId, Integer order);
}
