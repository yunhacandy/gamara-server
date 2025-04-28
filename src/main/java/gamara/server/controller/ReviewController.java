package gamara.server.controller;

import gamara.server.common.Response;
import gamara.server.common.exception.ImageException;
import gamara.server.dto.ReviewCreateRequest;
import gamara.server.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public Response<?> registerReview(@Valid @ModelAttribute ReviewCreateRequest request, long userId)
            throws ImageException {
        reviewService.registerReview(request, userId);
        return Response.createSuccessWithNoData("[Review Controller] Register Review");
    }
}
