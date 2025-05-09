package gamara.server.repository;

import gamara.server.domain.entity.redis.entity.BlackList;
import org.springframework.data.repository.CrudRepository;

public interface BlackListRepository extends CrudRepository<BlackList, String> {
}
