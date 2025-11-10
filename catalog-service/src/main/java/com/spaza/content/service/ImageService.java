package com.spaza.content.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    public String upload(MultipartFile file) {
        // TODO: Implement upload logic
        return "image-url-" + file.getOriginalFilename();
    }

    public byte[] get(String name) {
        // TODO: Implement get logic
        return new byte[0];
    }

    public void delete(String name) {
        // TODO: Implement delete logic
    }
}
