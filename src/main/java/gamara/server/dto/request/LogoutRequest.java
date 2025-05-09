package gamara.server.dto.request;

public record LogoutRequest(
        String accessToken,
        String refreshToken
) {
}
