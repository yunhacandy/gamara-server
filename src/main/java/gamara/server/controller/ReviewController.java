package gamara.server.controller;

import gamara.server.common.Response;
import gamara.server.common.exception.ImageException;
import gamara.server.domain.dto.ReviewDto;
import gamara.server.domain.dto.request.ReviewCreateRequest;
import gamara.server.security.jwt.AuthDetails;
import gamara.server.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "후기", description = "후기 관련된 api")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "후기 등록", description = "맵기 단계, 땅콩 소스 정도, 사골 유무, 얼얼함 단계 등 다양한 정보를 담은 후기를 등록합니다")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PostMapping
    public Response<?> registerReview(@Valid @ModelAttribute ReviewCreateRequest request,
                                      @AuthenticationPrincipal AuthDetails authDetails)
            throws ImageException {
        long userId = Long.parseLong(authDetails.getUserId());
        reviewService.registerReview(request, userId);
        return Response.createSuccessWithMessage("[Review Controller] Register Review");
    }

    @Operation(summary = "후기 삭제", description = "회원은 본인이 작성한 후기를 삭제할 수 있다")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @DeleteMapping("/{reviewId}")
    public Response<?> deleteReview(@PathVariable("reviewId") long reviewId,
                                    @AuthenticationPrincipal AuthDetails authDetails)
            throws ImageException {
        long userId = Long.parseLong(authDetails.getUserId());
        reviewService.deleteReview(reviewId, userId);
        return Response.createSuccessWithMessage("[Review Controller] Delete Review");
    }

    @Operation(summary = "특정 가게에 대한 후기 목록 조회", description = "특정 가게에 대한 후기를 조회해서 목록으로 가져올 수 있다.")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @GetMapping("/{storeId}")
    public Response<List<ReviewDto>> getReviewListByStore(@PathVariable("storeId") long storeId) {
        List<ReviewDto> reviewList = reviewService.getReviewListByStore(storeId);
        log.trace("[Review Controller] Get Review List By StoreId");
        return Response.createSuccess(reviewList);
    }
}
