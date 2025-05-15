package gamara.server.domain.dto.request;

public record WithdrawRequest(
        String accessToken,
        String refreshToken
) {
}
