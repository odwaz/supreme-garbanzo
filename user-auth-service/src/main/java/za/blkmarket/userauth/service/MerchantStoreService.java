package za.blkmarket.userauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import za.blkmarket.userauth.entity.MerchantStore;
import za.blkmarket.userauth.repository.MerchantStoreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MerchantStoreService {
    
    @Autowired
    private MerchantStoreRepository repository;
    
    public MerchantStore save(MerchantStore store) {
        return repository.save(store);
    }
    
    public Optional<MerchantStore> findByCode(String code) {
        return repository.findByCode(code);
    }
    
    public Optional<MerchantStore> findById(Long id) {
        return repository.findById(id);
    }
    
    public List<MerchantStore> findByParentId(Long parentId) {
        return repository.findByParentId(parentId);
    }
    
    public Page<MerchantStore> findAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }
    
    public boolean existsByCode(String code) {
        return repository.existsByCode(code);
    }
    
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
