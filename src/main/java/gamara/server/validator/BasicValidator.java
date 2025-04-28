package gamara.server.validator;

import gamara.server.common.exception.AppException;
import gamara.server.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BasicValidator {

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
