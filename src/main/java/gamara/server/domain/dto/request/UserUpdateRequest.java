package gamara.server.domain.dto.request;

import jakarta.validation.constraints.Pattern;

public record UserUpdateRequest(
        @Pattern(regexp = "[가-힣a-zA-Z0-9]{2,10}", message = "닉네임은 한글, 영어, 숫자를 조합해 2글자 이상, 10글자 이하입니다.")
        String userNickname
) {
}
