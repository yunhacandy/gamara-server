package gamara.server.service;

import gamara.server.converter.UserConverter;
import gamara.server.domain.dto.UserInfoDto;
import gamara.server.domain.dto.request.UserUpdateRequest;
import gamara.server.domain.entity.User;
import gamara.server.validator.BasicValidator;
import gamara.server.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BasicValidator basicValidator;
    private final EntityValidator entityValidator;

    @Transactional(readOnly = true)
    public UserInfoDto getUserInfo(long userId) {
        User user = entityValidator.getActiveUserById(userId);
        return UserConverter.toUserInfoDto(user);
    }

    @Transactional
    public void changeUserInfo(long userId, UserUpdateRequest userUpdateRequest) {
        basicValidator.validateIdRange(userId);
        User user = entityValidator.getActiveUserById(userId);
        user.updateUserInfo(userUpdateRequest.userNickname());
    }
}
