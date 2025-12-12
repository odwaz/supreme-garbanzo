package com.spaza.content.service;

import com.spaza.product.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class ImageService {

    @Autowired
    private FileUploadService fileUploadService;

    public String upload(MultipartFile file) {
        try {
            return fileUploadService.uploadFile(file);
        } catch (IOException e) {
            log.error("Failed to upload image: {}", e.getMessage());
            throw new RuntimeException("Image upload failed", e);
        }
    }

    public byte[] get(String name) {
        try {
            Path path = Paths.get("uploads/images/products", name);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            log.error("Failed to read image: {}", e.getMessage());
            return new byte[0];
        }
    }

    public void delete(String name) {
        if (!fileUploadService.deleteFile(name)) {
            log.warn("Failed to delete image: {}", name);
        }
    }
}
