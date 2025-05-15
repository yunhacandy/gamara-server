package gamara.server.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

public record WithdrawRequest(
        @NotBlank
        String accessToken,
        @NotBlank
        String refreshToken
) {
}
