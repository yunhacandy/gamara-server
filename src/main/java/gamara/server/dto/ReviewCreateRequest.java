package gamara.server.dto;

import gamara.server.enums.SpicyLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "리뷰 작성 요청")
public record ReviewCreateRequest(
        long storeId,
        String context,
        @NotNull
        SpicyLevel spicyLevel,
        int peanutLevel,
        boolean hasSagol,
        int tingleLevel,
        MultipartFile image
) {
}
