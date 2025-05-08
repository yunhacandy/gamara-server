package gamara.server.validator;

import gamara.server.common.exception.AppException;
import gamara.server.common.exception.ErrorCode;
import gamara.server.domain.repository.StoreRepository;
import gamara.server.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityValidator {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public void validateUserExists(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
    }

    public void validateStoreExists(long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new AppException(ErrorCode.STORE_NOT_FOUND);
        }
    }
}
