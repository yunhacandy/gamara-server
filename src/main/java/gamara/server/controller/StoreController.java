package gamara.server.controller;

import gamara.server.common.Response;
import gamara.server.security.jwt.AuthDetails;
import gamara.server.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "가게", description = "가게 및 가게 추천 관련된 api")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    @Operation(summary = "가게 추천 등록", description = "사용자는 가게당 한번의 추천을 누를 수 있습니다.")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PostMapping("/{storeId}/recommend")
    public Response<?> recommend(@PathVariable("storeId") long storeId, @AuthenticationPrincipal AuthDetails authDetails) {
        long userId = Long.parseLong(authDetails.getUserId());
        storeService.recommendStore(userId, storeId);
        return Response.createSuccessWithMessage("[Store Controller] Register Store Recommend");
    }
}
