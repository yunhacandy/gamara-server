package gamara.server.infrastructure.client;

import gamara.server.dto.response.OAuthUserInfoResponse;
import gamara.server.enums.Provider;

public interface OAuthApiClient {
    Provider oAuthProvider();   //todo: 함수명 수정?

    OAuthUserInfoResponse requestOauthUserInfo(String accessToken);
}
