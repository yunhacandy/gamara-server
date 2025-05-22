package gamara.server.repository;

import gamara.server.domain.entity.StoreRecommend;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRecommendRepository extends JpaRepository<StoreRecommend, Long> {
    boolean existsByUserIdAndStoreId(long userId, long storeId);

    Optional<StoreRecommend> findByUserIdAndStoreId(long userId, long storeId);
}
