package gamara.server.dto;

public record LoginResultDto(
        String accessToken,
        String refreshToken,
        boolean isSignUp
) {
}