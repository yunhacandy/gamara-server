package gamara.server.service;

import gamara.server.auth.RequestOAuthUserInfoService;
import gamara.server.converter.AuthConverter;
import gamara.server.domain.entity.User;
import gamara.server.dto.LoginResultDto;
import gamara.server.dto.request.KakaoLoginRequest;
import gamara.server.dto.response.OAuthUserInfoResponse;
import gamara.server.enums.Provider;
import gamara.server.redis.entity.RefreshToken;
import gamara.server.redis.repository.BlackListRepository;
import gamara.server.redis.repository.RefreshTokenRepository;
import gamara.server.security.jwt.JwtProvider;
import gamara.server.validator.BasicValidator;
import gamara.server.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RequestOAuthUserInfoService requestOAuthUserInfoService;
    private final UserRepository userRepository;
    private final BasicValidator basicValidator;
    private final EntityValidator entityValidator;

    public LoginResultDto loginKakao(KakaoLoginRequest request) {
        // 1. accessToken으로 사용자 정보 요청
        OAuthUserInfoResponse userInfo = requestOAuthUserInfoService.request(Provider.KAKAO, request.accessToken());

        String email = userInfo.getEmail();
        String nickname = userInfo.getNickname();
        Provider provider = userInfo.getOAuthProvider();

        // 2. 이메일로 기존 사용자 조회 및 신규 등록
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(AuthConverter.toUserEntity(email, nickname, provider)));

        // 3. JWT 발급
        String accessToken = jwtProvider.generateAccessToken(user.getId(), "ROLE_USER");
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());

        // 4. converter 사용해서 dto로 변환
        return AuthConverter.toLoginResultDto(user, accessToken, refreshToken);
    }

    public void validateRegisteredUser(long userId) {
        basicValidator.validateIdRange(userId);
        entityValidator.validateUserExists(userId);
    }
}
