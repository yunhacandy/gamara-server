package gamara.server.repository;

import gamara.server.domain.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserIdAndStoreId(long userId, long storeId);

    List<Review> findAllByStoreId(long storeId);

    List<Review> findAllByStoreIdOrderByCreatedAtDesc(long storeId);
    List<Review> findAllByStoreIdOrderByCreatedAtAsc(long storeId);
}
