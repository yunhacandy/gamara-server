package gamara.server.domain.dto.response;

import gamara.server.enums.Provider;

public interface OAuthUserInfoResponse {
    String getEmail();

    String getNickname();

    Provider getOAuthProvider();
}
