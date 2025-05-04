package gamara.server.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import gamara.server.enums.Provider;

public record KakaoUserInfoResponse(
        @JsonProperty("kakao_account") KakaoAccount kakaoAccount
) implements OAuthUserInfoResponse {

    @Override
    public String getEmail() {
        return kakaoAccount.email();
    }

    @Override
    public String getNickname() {
        return kakaoAccount.profile().nickname();
    }

    @Override
    public Provider getOAuthProvider() {
        return Provider.KAKAO;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record KakaoAccount(String email, Profile profile) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Profile(String nickname) {
    }
}
