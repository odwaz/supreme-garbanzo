package za.blkmarket.userauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import za.blkmarket.userauth.entity.Group;
import za.blkmarket.userauth.entity.User;
import za.blkmarket.userauth.repository.UserRepository;
import za.blkmarket.userauth.repository.GroupRepository;
import za.blkmarket.userauth.repository.MerchantStoreRepository;
import za.blkmarket.userauth.entity.MerchantStore;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private MerchantStoreRepository merchantStoreRepository;
    
    @Autowired
    private GroupRepository groupRepository;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@spaza.com").isPresent()) {
            return;
        }
        
        Group adminGroup = new Group();
        adminGroup.setName("ADMIN");
        adminGroup = groupRepository.save(adminGroup);
        
        MerchantStore store = new MerchantStore();
        store.setCode("DEFAULT");
        store.setName("Main Store");
        store.setEmail("store@spaza.com");
        store.setPhone("+27123456789");
        store = merchantStoreRepository.save(store);
        
        User admin = new User();
        admin.setEmail("admin@spaza.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setActive(true);
        admin.setCreatedDate(LocalDateTime.now());
        admin.setUpdatedDate(LocalDateTime.now());
        admin.setGroups(Arrays.asList(adminGroup));
        admin.setMerchantStore(store);
        userRepository.save(admin);
    }
}
