package gamara.server.converter;

import gamara.server.domain.dto.ReviewDto;
import gamara.server.domain.entity.Review;
import gamara.server.domain.dto.request.ReviewCreateRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewConverter {

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

    public static ReviewDto toDto(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getContext(),
                review.getSpicyLevel(),
                review.getPeanutLevel(),
                review.isHasSagol(),
                review.getTingleLevel(),
                review.getImageUrl(),
                review.getCreatedAt()
        );
    }
}
