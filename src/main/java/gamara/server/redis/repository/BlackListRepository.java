package gamara.server.redis.repository;

import gamara.server.redis.entity.BlackList;
import org.springframework.data.repository.CrudRepository;

public interface BlackListRepository extends CrudRepository<BlackList, String> {
}
