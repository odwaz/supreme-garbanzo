package za.blkmarket.userauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.blkmarket.userauth.dto.PersistableMerchantStore;
import za.blkmarket.userauth.dto.ReadableMerchantStore;
import za.blkmarket.userauth.entity.MerchantStore;
import za.blkmarket.userauth.service.MerchantStoreService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class MerchantStoreController {
    
    @Autowired
    private MerchantStoreService storeService;
    
    @Autowired
    private za.blkmarket.userauth.repository.UserRepository userRepository;
    
    @Autowired
    private za.blkmarket.userauth.security.JwtTokenProvider jwtTokenProvider;
    
    @PostMapping("/private/stores")
    public ResponseEntity<ReadableMerchantStore> create(@RequestBody PersistableMerchantStore dto) {
        if (storeService.existsByCode(dto.getCode())) {
            return ResponseEntity.badRequest().build();
        }
        
        MerchantStore store = new MerchantStore();
        store.setCode(dto.getCode());
        store.setName(dto.getName());
        store.setEmail(dto.getEmail());
        store.setPhone(dto.getPhone());
        if (dto.getAddress() != null) {
            store.setAddress(dto.getAddress().getAddress());
            store.setCity(dto.getAddress().getCity());
            store.setStateProvince(dto.getAddress().getStateProvince());
            store.setPostalCode(dto.getAddress().getPostalCode());
            store.setCountry(dto.getAddress().getCountry());
        }
        
        if (dto.getParentId() != null) {
            MerchantStore parent = storeService.findById(dto.getParentId())
                .orElseThrow(() -> new RuntimeException("Parent store not found"));
            store.setParent(parent);
        }
        
        MerchantStore saved = storeService.save(store);
        return ResponseEntity.ok(toReadable(saved));
    }
    
    @PutMapping("/private/stores/{code}")
    public ResponseEntity<ReadableMerchantStore> update(@PathVariable String code, @RequestBody PersistableMerchantStore dto) {
        MerchantStore store = storeService.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Store not found"));
        
        store.setName(dto.getName());
        store.setEmail(dto.getEmail());
        store.setPhone(dto.getPhone());
        if (dto.getAddress() != null) {
            store.setAddress(dto.getAddress().getAddress());
            store.setCity(dto.getAddress().getCity());
            store.setStateProvince(dto.getAddress().getStateProvince());
            store.setPostalCode(dto.getAddress().getPostalCode());
            store.setCountry(dto.getAddress().getCountry());
        }
        
        MerchantStore saved = storeService.save(store);
        return ResponseEntity.ok(toReadable(saved));
    }
    
    @DeleteMapping("/private/stores/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        MerchantStore store = storeService.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Store not found"));
        storeService.deleteById(store.getId());
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/private/stores/{code}")
    public ResponseEntity<ReadableMerchantStore> get(@PathVariable String code) {
        if ("undefined".equals(code) || "null".equals(code)) {
            return ResponseEntity.badRequest().build();
        }
        MerchantStore store = storeService.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Store not found"));
        return ResponseEntity.ok(toReadable(store));
    }
    
    @GetMapping("/private/stores")
    public ResponseEntity<List<ReadableMerchantStore>> list(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int count) {
        Page<MerchantStore> stores = storeService.findAll(page, count);
        List<ReadableMerchantStore> result = stores.getContent().stream()
            .map(this::toReadable)
            .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/private/stores/{code}/children")
    public ResponseEntity<List<ReadableMerchantStore>> children(@PathVariable String code) {
        MerchantStore parent = storeService.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Store not found"));
        List<MerchantStore> children = storeService.findByParentId(parent.getId());
        List<ReadableMerchantStore> result = children.stream()
            .map(this::toReadable)
            .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/store/{code}")
    public ResponseEntity<ReadableMerchantStore> publicGet(@PathVariable String code) {
        MerchantStore store = storeService.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Store not found"));
        return ResponseEntity.ok(toReadable(store));
    }
    
    @GetMapping("/private/user/profile")
    public ResponseEntity<java.util.Map<String, Object>> getProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtTokenProvider.getUsernameFromToken(token);
        
        za.blkmarket.userauth.entity.User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        java.util.Map<String, Object> profile = new java.util.HashMap<>();
        profile.put("id", user.getId());
        profile.put("email", user.getEmail());
        profile.put("name", user.getFirstName() + " " + user.getLastName());
        profile.put("permissions", user.getGroups() != null ? 
            user.getGroups().stream().map(g -> g.getName()).collect(java.util.stream.Collectors.toList()) : 
            java.util.List.of());
        
        if (user.getMerchantStore() != null) {
            profile.put("store", toReadable(user.getMerchantStore()));
        }
        
        return ResponseEntity.ok(profile);
    }
    
    private ReadableMerchantStore toReadable(MerchantStore store) {
        ReadableMerchantStore dto = new ReadableMerchantStore();
        dto.setId(store.getId());
        dto.setCode(store.getCode());
        dto.setName(store.getName());
        dto.setEmail(store.getEmail());
        dto.setPhone(store.getPhone());
        if (store.getAddress() != null || store.getCity() != null) {
            za.blkmarket.userauth.dto.ReadableAddress addr = new za.blkmarket.userauth.dto.ReadableAddress();
            addr.setAddress(store.getAddress());
            addr.setCity(store.getCity());
            addr.setStateProvince(store.getStateProvince());
            addr.setPostalCode(store.getPostalCode());
            addr.setCountry(store.getCountry());
            dto.setAddress(addr);
        }
        if (store.getParent() != null) {
            dto.setParentId(store.getParent().getId());
            dto.setParentCode(store.getParent().getCode());
        }
        return dto;
    }
}
