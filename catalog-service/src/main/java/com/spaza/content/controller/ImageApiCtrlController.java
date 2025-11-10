package com.spaza.content.controller;

import com.spaza.content.api.ImageApiCtrl;
import com.spaza.content.delegate.ImageApiCtrlDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
public class ImageApiCtrlController implements ImageApiCtrl {

    @Autowired
    private ImageApiCtrlDelegate delegate;

    @Override
    public ResponseEntity<String> upload(MultipartFile file) {
        return delegate.upload(file);
    }

    @Override
    public ResponseEntity<byte[]> get(String name) {
        return delegate.get(name);
    }

    @Override
    public ResponseEntity<Void> delete(String name) {
        return delegate.delete(name);
    }
}
