package gamara.server.service;

import gamara.server.common.exception.AppException;
import gamara.server.common.exception.ErrorCode;
import gamara.server.converter.StoreConverter;
import gamara.server.domain.entity.Store;
import gamara.server.domain.entity.StoreRecommend;
import gamara.server.repository.StoreRecommendRepository;
import gamara.server.repository.StoreRepository;
import gamara.server.validator.BasicValidator;
import gamara.server.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreRecommendRepository storeRecommendRepository;
    private final BasicValidator basicValidator;
    private final EntityValidator entityValidator;

    @Transactional
    public void recommendStore(long userId, long storeId) {
        basicValidator.validateIdRange(userId);
        basicValidator.validateIdRange(storeId);
        entityValidator.validateUserIsActive(userId);

        Store store = storeRepository.findById(storeId).orElseThrow(() -> new AppException(ErrorCode.STORE_NOT_FOUND));

        if (storeRecommendRepository.existsByUserIdAndStoreId(userId, storeId)) {
            throw new AppException(ErrorCode.ALREADY_RECOMMENDED);
        }

        StoreRecommend storeRecommend = StoreConverter.toEntity(userId, storeId);
        storeRecommendRepository.save(storeRecommend);
        store.incrementRecommendCount();
    }
}
