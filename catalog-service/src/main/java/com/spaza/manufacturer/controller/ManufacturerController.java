package com.spaza.manufacturer.controller;

import com.spaza.manufacturer.entity.Manufacturer;
import com.spaza.manufacturer.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/private/manufacturers")
public class ManufacturerController {
    
    @Autowired
    private ManufacturerService service;
    
    @PostMapping
    public ResponseEntity<Manufacturer> create(@RequestBody Manufacturer manufacturer) {
        return ResponseEntity.ok(service.save(manufacturer));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Manufacturer> update(@PathVariable Long id, @RequestBody Manufacturer manufacturer) {
        manufacturer.setId(id);
        return ResponseEntity.ok(service.save(manufacturer));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Manufacturer> get(@PathVariable Long id) {
        return service.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Manufacturer>> list(@RequestParam Long merchantId) {
        return ResponseEntity.ok(service.findByMerchantId(merchantId));
    }
}
