package gamara.server.service;

import gamara.server.auth.RequestOAuthUserInfoService;
import gamara.server.common.exception.AppException;
import gamara.server.common.exception.ErrorCode;
import gamara.server.converter.AuthConverter;
import gamara.server.domain.entity.User;
import gamara.server.domain.repository.UserRepository;
import gamara.server.dto.LoginResultDto;
import gamara.server.dto.ReissueResultDto;
import gamara.server.dto.request.KakaoLoginRequest;
import gamara.server.dto.response.OAuthUserInfoResponse;
import gamara.server.enums.Provider;
import gamara.server.redis.entity.RefreshToken;
import gamara.server.redis.repository.RefreshTokenRepository;
import gamara.server.security.jwt.JwtProvider;
import gamara.server.validator.BasicValidator;
import gamara.server.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RequestOAuthUserInfoService requestOAuthUserInfoService;
    private final UserRepository userRepository;
    private final BasicValidator basicValidator;
    private final EntityValidator entityValidator;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-time}")
    private Long refreshTokenTime;

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

        // 4. refreshToken Redis 저장
        RefreshToken tokenEntity = AuthConverter.toRefreshTokenEntity(
                user.getId(),
                refreshToken,
                refreshTokenTime
        );
        refreshTokenRepository.save(tokenEntity);

        // 5. converter 사용해서 dto로 변환
        return AuthConverter.toLoginResultDto(user, accessToken, refreshToken);
    }

    public void validateRegisteredUser(long userId) {
        basicValidator.validateIdRange(userId);
        entityValidator.validateUserExists(userId);
    }

    public ReissueResultDto reissueToken(String refreshToken) {
        // 1. refreshToken 유효성 검증 및 userId 추출
        Long userId = jwtProvider.parseRefreshToken(refreshToken);

        // 2. Redis 저장값 확인 (id 기준)
        RefreshToken storedRefreshToken = refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        // 3. 값 비교
        if (!storedRefreshToken.getRefreshToken().equals(refreshToken)) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 4. 기존 refreshToken 삭제
        refreshTokenRepository.deleteById(userId);

        // 5. 새 토큰 발급
        String newAccessToken = jwtProvider.generateAccessToken(userId, "ROLE_USER");
        String newRefreshToken = jwtProvider.generateRefreshToken(userId);

        // 6. 새 refreshToken Redis 저장
        RefreshToken tokenEntity = AuthConverter.toRefreshTokenEntity(
                userId,
                newRefreshToken,
                refreshTokenTime
        );
        refreshTokenRepository.save(tokenEntity);

        // 7. 결과 반환
        return new ReissueResultDto(newAccessToken, newRefreshToken);
    }
}
