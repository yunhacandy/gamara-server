package gamara.server.controller;

import gamara.server.common.Response;
import gamara.server.domain.dto.LoginResultDto;
import gamara.server.domain.dto.ReissueResultDto;
import gamara.server.domain.dto.request.KakaoLoginRequest;
import gamara.server.domain.dto.request.LogoutRequest;
import gamara.server.domain.dto.request.ReissueRequest;
import gamara.server.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PostMapping("/kakao/login")
    public Response<LoginResultDto> loginKakao(@RequestBody KakaoLoginRequest request) {
        LoginResultDto response = authService.loginKakao(request);
        return Response.createSuccess("[Auth Controller] Complete Kakao Login", response);
    }

    @Operation(summary = "access token 재발급", description = "만료된 access token 을 refresh token으로 재발급 하는 API")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PostMapping("/reissue")
    public Response<ReissueResultDto> reissueToken(@RequestBody ReissueRequest reissueRequest) {
        return Response.createSuccess("[Auth Controller] Complete Reissue",
                authService.reissueToken(reissueRequest.refreshToken()));
    }

    @Operation(summary = "로그아웃", description = "로그아웃 하는 API access token과 refresh token 을 body에 담아 보내고 서버에서 토큰을 만료시킨다.")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PostMapping("/logout")
    public Response<?> logout(@RequestBody LogoutRequest logoutRequest) {
        authService.logout(logoutRequest);
        return Response.createSuccessWithMessage("[Auth Controller] Complete Logout");
    }
}
