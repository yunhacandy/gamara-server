package gamara.server.infrastructure.client;

import gamara.server.dto.response.KakaoUserInfoResponse;
import gamara.server.dto.response.OAuthUserInfoResponse;
import gamara.server.enums.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {

    private final RestClient restClient;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoUserInfoUrl;

    @Override
    public Provider oAuthProvider() {
        return Provider.KAKAO;
    }

    @Override
    public OAuthUserInfoResponse requestOauthUserInfo(String accessToken) {
        return restClient.get()
                .uri(kakaoUserInfoUrl)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .body(KakaoUserInfoResponse.class);
    }
}
