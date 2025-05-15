package gamara.server.controller;

import gamara.server.common.Response;
import gamara.server.domain.dto.UserInfoDto;
import gamara.server.security.jwt.AuthDetails;
import gamara.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저", description = "유저 관련된 api")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 정보 조회", description = "현재 로그인된 사용자가 자신의 정보를 조회합니다")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @GetMapping
    public Response<UserInfoDto> getUserInfo(@AuthenticationPrincipal AuthDetails authDetails) {
        long userId = Long.parseLong(authDetails.getUserId());
        return Response.createSuccess("[User Controller] Get User Info", userService.getUserInfo(userId));
    }
}
