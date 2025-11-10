package com.spaza.content.delegate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageApiCtrlDelegate {
    ResponseEntity<String> upload(MultipartFile file);
    ResponseEntity<byte[]> get(String name);
    ResponseEntity<Void> delete(String name);
}
