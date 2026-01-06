package za.blkmarket.userauth.repository;

import za.blkmarket.userauth.entity.MerchantStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantStoreRepository extends JpaRepository<MerchantStore, Long> {
    Optional<MerchantStore> findByCode(String code);
    List<MerchantStore> findByParentId(Long parentId);
    boolean existsByCode(String code);
}
