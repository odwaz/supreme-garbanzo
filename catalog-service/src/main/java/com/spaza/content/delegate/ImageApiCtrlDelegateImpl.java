package com.spaza.content.delegate;

import com.spaza.content.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageApiCtrlDelegateImpl implements ImageApiCtrlDelegate {

    private final ImageService imageService;

    @Override
    public ResponseEntity<String> upload(MultipartFile file) {
        return ResponseEntity.ok(imageService.upload(file));
    }

    @Override
    public ResponseEntity<byte[]> get(String name) {
        return ResponseEntity.ok(imageService.get(name));
    }

    @Override
    public ResponseEntity<Void> delete(String name) {
        imageService.delete(name);
        return ResponseEntity.ok().build();
    }
}
