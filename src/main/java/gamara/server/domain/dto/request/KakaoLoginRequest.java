package gamara.server.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

public record KakaoLoginRequest(
        @NotBlank
        String accessToken
) {
}
