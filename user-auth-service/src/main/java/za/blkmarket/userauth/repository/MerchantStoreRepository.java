package za.blkmarket.userauth.repository;

import za.blkmarket.userauth.entity.MerchantStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MerchantStoreRepository extends JpaRepository<MerchantStore, Long> {
    List<MerchantStore> findByMerchantCode(String merchantCode);
}
