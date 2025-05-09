package gamara.server.infrastructure.client;

import gamara.server.dto.response.OAuthUserInfoResponse;
import gamara.server.enums.Provider;

public interface OAuthApiClient {
    Provider oAuthProvider();

    OAuthUserInfoResponse requestOauthUserInfo(String accessToken);
}
