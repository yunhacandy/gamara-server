package gamara.server.converter;

import gamara.server.domain.entity.Review;
import gamara.server.dto.request.ReviewCreateRequest;
import java.time.LocalDateTime;

public class ReviewDtoConverter {
    public static Review toEntity(long userId, ReviewCreateRequest request, String imageUrl) {
        return Review.builder()
                .userId(userId)
                .storeId(request.storeId())
                .context(request.context())
                .spicyLevel(request.spicyLevel())
                .peanutLevel(request.peanutLevel())
                .hasSagol(request.hasSagol())
                .tingleLevel(request.tingleLevel())
                .createdAt(LocalDateTime.now())
                .imageUrl(imageUrl)
                .build();
    }
}
