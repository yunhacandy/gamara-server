package gamara.server.controller;

import gamara.server.common.Response;
import gamara.server.dto.request.KakaoLoginRequest;
import gamara.server.dto.LoginResultDto;
import gamara.server.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "auth", description = "auth 관련된 api")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "카카오 로그인", description = "카카오에서 받은 액세스 토큰을 통해 회원가입 또는 로그인하는 API")
    @PostMapping("/kakao/login")
    public Response<LoginResultDto> loginKakao(@RequestBody KakaoLoginRequest request) {
        LoginResultDto response = authService.loginKakao(request);
        return Response.createSuccess(response);
    }
}
