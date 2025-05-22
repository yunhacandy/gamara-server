package gamara.server.domain.dto;

import gamara.server.enums.SpicyLevel;
import java.time.LocalDateTime;

public record ReviewDto(
        long reviewId,
        String context,
        SpicyLevel spicyLevel,
        int peanutLevel,
        boolean hasSagol,
        int tingleLevel,
        String imageUrl,
        LocalDateTime createdAt
) {
}
