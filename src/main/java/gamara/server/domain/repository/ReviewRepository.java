package gamara.server.domain.repository;

import gamara.server.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserIdAndStoreId(long userId, long storeId);
}
