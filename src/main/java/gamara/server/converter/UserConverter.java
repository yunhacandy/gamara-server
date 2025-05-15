package gamara.server.converter;

import gamara.server.domain.dto.UserInfoDto;
import gamara.server.domain.entity.User;

public class UserConverter {

    public static UserInfoDto toUserInfoDto(User user) {
        return new UserInfoDto(user.getEmail(), user.getNickname());
    }
}
