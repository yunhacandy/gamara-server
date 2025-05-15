package gamara.server.service;

import gamara.server.common.exception.AppException;
import gamara.server.common.exception.ErrorCode;
import gamara.server.converter.AuthConverter;
import gamara.server.domain.dto.LoginResultDto;
import gamara.server.domain.dto.ReissueResultDto;
import gamara.server.domain.dto.request.KakaoLoginRequest;
import gamara.server.domain.dto.request.LogoutRequest;
import gamara.server.domain.dto.request.WithdrawRequest;
import gamara.server.domain.dto.response.OAuthUserInfoResponse;
import gamara.server.domain.entity.User;
import gamara.server.domain.entity.redis.entity.BlackList;
import gamara.server.domain.entity.redis.entity.RefreshToken;
import gamara.server.enums.Provider;
import gamara.server.repository.BlackListRepository;
import gamara.server.repository.RefreshTokenRepository;
import gamara.server.repository.UserRepository;
import gamara.server.security.jwt.JwtProvider;
import gamara.server.security.jwt.properties.JwtProperties;
import gamara.server.validator.BasicValidator;
import gamara.server.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final RequestOAuthUserInfoService requestOAuthUserInfoService;
    private final UserRepository userRepository;
    private final BasicValidator basicValidator;
    private final EntityValidator entityValidator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListRepository blackListRepository;

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
                jwtProperties.getRefreshTokenTime()
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
                jwtProperties.getRefreshTokenTime()
        );
        refreshTokenRepository.save(tokenEntity);

        // 7. 결과 반환
        return new ReissueResultDto(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(LogoutRequest logoutRequest) {
        // 1. accessToken → 블랙리스트 등록
        setBlackList(logoutRequest.accessToken());

        // 2. refreshToken → Redis에서 삭제
        deleteRefreshToken(logoutRequest.refreshToken());
    }

    @Transactional
    public void withdraw(long userId, WithdrawRequest withdrawRequest) {
        basicValidator.validateIdRange(userId);

        // 1. accessToken 블랙리스트 처리
        setBlackList(withdrawRequest.accessToken());

        // 2. RefreshToken 삭제
        RefreshToken token = refreshTokenRepository.findByRefreshToken(withdrawRequest.refreshToken())
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
        refreshTokenRepository.delete(token);

        // 3. 회원 삭제 처리
        User user = entityValidator.getActiveUserById(userId);
        user.markAsDeleted();
    }

    public boolean isBlocked(String accessToken) {
        return blackListRepository.findById(accessToken).isPresent();
    }

    private void setBlackList(String accessToken) {
        long ttl = jwtProvider.getExpiration(accessToken);
        BlackList blacklist = AuthConverter.toBlackList(accessToken, ttl);
        blackListRepository.save(blacklist);
    }

    private void deleteRefreshToken(String refreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        refreshTokenRepository.delete(refreshTokenEntity);
    }

}
