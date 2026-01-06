package com.spaza.manufacturer.service;

import com.spaza.manufacturer.entity.Manufacturer;
import com.spaza.manufacturer.repository.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerService {
    
    private final ManufacturerRepository repository;

    public ManufacturerService(ManufacturerRepository repository) {
        this.repository = repository;
    }
    
    public Manufacturer save(Manufacturer manufacturer) {
        return repository.save(manufacturer);
    }
    
    public Optional<Manufacturer> findById(Long id) {
        return repository.findById(id);
    }
    
    public List<Manufacturer> findByMerchantId(Long merchantId) {
        return repository.findByMerchantId(merchantId);
    }
    
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
