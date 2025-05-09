package gamara.server.domain.dto.request;

public record LogoutRequest(
        String accessToken,
        String refreshToken
) {
}
