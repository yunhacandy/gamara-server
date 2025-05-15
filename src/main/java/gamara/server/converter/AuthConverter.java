package gamara.server.converter;

import gamara.server.domain.entity.User;
import gamara.server.domain.dto.LoginResultDto;
import gamara.server.enums.Provider;
import gamara.server.domain.entity.redis.entity.BlackList;
import gamara.server.domain.entity.redis.entity.RefreshToken;
import java.time.LocalDateTime;

public class AuthConverter {

    public static LoginResultDto toLoginResultDto(User user, String accessToken, String refreshToken) {
        boolean isSignUp = user.getCreatedAt().equals(user.getModifiedAt());
        return new LoginResultDto(accessToken, refreshToken, isSignUp);
    }

    public static User toUserEntity(String email, String nickname, Provider provider) {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .provider(provider)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .deleted(false)
                .build();
    }

    public static RefreshToken toRefreshTokenEntity(Long userId, String refreshToken, long ttl) {
        return RefreshToken.builder()
                .id(userId)
                .refreshToken(refreshToken)
                .ttl(ttl)
                .build();
    }

    public static BlackList toBlackList(String token, long ttl) {
        return BlackList.builder()
                .id(token)
                .ttl(ttl)
                .build();
    }
}
