package gamara.server.service;

import gamara.server.common.exception.AppException;
import gamara.server.common.exception.ErrorCode;
import gamara.server.converter.UserConverter;
import gamara.server.domain.dto.UserInfoDto;
import gamara.server.domain.dto.request.UserUpdateRequest;
import gamara.server.domain.entity.User;
import gamara.server.repository.UserRepository;
import gamara.server.validator.BasicValidator;
import gamara.server.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BasicValidator basicValidator;
    private final EntityValidator entityValidator;

    @Transactional(readOnly = true)
    public UserInfoDto getUserInfo(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) {
            throw new AppException(ErrorCode.USER_ALREADY_DELETED);
        }
        return UserConverter.toUserInfoDto(user);
    }

    @Transactional
    public void changeUserInfo(long userId, UserUpdateRequest userUpdateRequest) {
        basicValidator.validateIdRange(userId);
        entityValidator.validateUserIsActive(userId);
        userRepository.getReferenceById(userId).updateUserInfo(userUpdateRequest.userNickname());
    }
}
