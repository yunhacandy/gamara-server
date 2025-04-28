package gamara.server.service;

import gamara.server.common.exception.AppException;
import gamara.server.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidateService {

    public void checkValidIdRange(long id) {
        if (id <= 0) {
            throw new AppException(ErrorCode.INVALID_ID_RANGE);
        }
    }

    public void checkValidLevelRange(int level) {
        if (level < 1 || level > 5) {
            throw new AppException(ErrorCode.INVALID_LEVEL_RANGE);
        }
    }
}
