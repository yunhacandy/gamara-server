package gamara.server.service;

import gamara.server.common.exception.AppException;
import gamara.server.common.exception.ErrorCode;
import gamara.server.converter.UserConverter;
import gamara.server.domain.dto.UserInfoDto;
import gamara.server.domain.entity.User;
import gamara.server.repository.UserRepository;
import gamara.server.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final EntityValidator entityValidator;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserInfoDto getUserInfo(long userId) {
        entityValidator.validateUserIsActive(userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return UserConverter.toUserInfoDto(user);
    }
}
