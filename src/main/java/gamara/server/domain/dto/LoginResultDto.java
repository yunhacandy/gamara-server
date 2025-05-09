package gamara.server.domain.dto;

public record LoginResultDto(
        String accessToken,
        String refreshToken,
        boolean isSignUp
) {
}