package gamara.server.auth;

import gamara.server.dto.response.OAuthUserInfoResponse;
import gamara.server.enums.Provider;
import gamara.server.infrastructure.client.OAuthApiClient;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class RequestOAuthUserInfoService {

    private final Map<Provider, OAuthApiClient> clients = new EnumMap<>(Provider.class);

    public RequestOAuthUserInfoService(List<OAuthApiClient> clientList) {
        for (OAuthApiClient client : clientList) {
            clients.put(client.oAuthProvider(), client);
        }
    }

    public OAuthUserInfoResponse request(Provider provider, String accessToken) {
        OAuthApiClient client = clients.get(provider);
        if (client == null) {
            throw new IllegalArgumentException("지원하지 않는 OAuth Provider입니다: " + provider);
        }
        return client.requestOauthUserInfo(accessToken);
    }
}
