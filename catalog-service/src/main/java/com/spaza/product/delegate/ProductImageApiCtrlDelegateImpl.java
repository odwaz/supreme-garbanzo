package com.spaza.product.delegate;

import com.spaza.product.model.Product;
import com.spaza.product.model.ProductImage;
import com.spaza.product.repository.ProductImageRepository;
import com.spaza.product.repository.ProductRepository;
import com.spaza.product.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductImageApiCtrlDelegateImpl implements ProductImageApiCtrlDelegate {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductImageRepository productImageRepository;
    
    @Autowired
    private FileUploadService fileUploadService;

    @Override
    public ResponseEntity<Void> uploadImage(Long id, List<MultipartFile> file, Boolean defaultImage, Integer order) {
        try {
            Optional<Product> productOpt = productRepository.findById(id);
            if (!productOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            Product product = productOpt.get();
            
            for (int i = 0; i < file.size(); i++) {
                MultipartFile uploadFile = file.get(i);
                if (!uploadFile.isEmpty()) {
                    String imageUrl = fileUploadService.uploadFile(uploadFile);
                    
                    ProductImage productImage = new ProductImage();
                    productImage.setProduct(product);
                    productImage.setImageName(uploadFile.getOriginalFilename());
                    productImage.setImageUrl(imageUrl);
                    productImage.setSortOrder(order != null ? order + i : i + 1);
                    productImage.setDefaultImage(defaultImage != null && defaultImage && i == 0);
                    
                    productImageRepository.save(productImage);
                }
            }
            
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<Object>> getImages(Long productId) {
        List<ProductImage> images = productImageRepository.findByProduct_IdOrderBySortOrderAsc(productId);
        List<Object> imageObjects = images.stream()
                .map(img -> {
                    Map<String, Object> imageMap = new HashMap<>();
                    imageMap.put("id", img.getId());
                    imageMap.put("name", img.getImageName());
                    imageMap.put("path", img.getImageUrl());
                    imageMap.put("defaultImage", img.getDefaultImage());
                    imageMap.put("sortOrder", img.getSortOrder());
                    return imageMap;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(imageObjects);
    }

    @Override
    public ResponseEntity<Void> deleteImage(Long id, Long imageId) {
        Optional<ProductImage> imageOpt = productImageRepository.findById(imageId);
        if (imageOpt.isPresent()) {
            ProductImage image = imageOpt.get();
            String filename = image.getImageUrl().substring(image.getImageUrl().lastIndexOf("/") + 1);
            fileUploadService.deleteFile(filename);
            productImageRepository.delete(image);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Void> updateImageDetails(Long id, Long imageId, Integer order) {
        Optional<ProductImage> imageOpt = productImageRepository.findById(imageId);
        if (imageOpt.isPresent()) {
            ProductImage image = imageOpt.get();
            if (order != null) {
                image.setSortOrder(order);
            }
            productImageRepository.save(image);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
