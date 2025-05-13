package gamara.server.service;

import gamara.server.common.exception.AppException;
import gamara.server.common.exception.ErrorCode;
import gamara.server.common.exception.ImageException;
import gamara.server.converter.ReviewConverter;
import gamara.server.domain.entity.Review;
import gamara.server.domain.dto.request.ReviewCreateRequest;
import gamara.server.repository.ReviewRepository;
import gamara.server.validator.BasicValidator;
import gamara.server.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

    private static final String IMAGE_DIR = "review";
    private static final String S3_URL_PREFIX =
            "https://" + System.getenv("AWS_S3_BUCKET") + ".s3.ap-northeast-2.amazonaws.com/";

    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;
    private final BasicValidator basicValidator;
    private final EntityValidator entityValidator;

    @Transactional
    public void registerReview(ReviewCreateRequest request, long userId) throws ImageException {
        basicValidator.validateIdRange(userId);
        basicValidator.validateIdRange(request.storeId());
        basicValidator.validateLevelRange(request.peanutLevel());
        basicValidator.validateLevelRange(request.tingleLevel());

        entityValidator.validateUserIsActive(userId);
        entityValidator.validateStoreExists(request.storeId());

        if (reviewRepository.existsByUserIdAndStoreId(userId, request.storeId())) {
            throw new AppException(ErrorCode. REVIEW_ALREADY_EXISTS);
        }

        String imageUrl = null;
        MultipartFile imageFile = request.image();
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = s3Service.uploadFile(imageFile, IMAGE_DIR);
        }

        Review review = ReviewConverter.toEntity(userId, request, imageUrl);
        reviewRepository.save(review);

        log.trace("Completed registering review: reviewId={}", review.getId());
    }

    @Transactional
    public void deleteReview(long reviewId, long userId) throws ImageException {
        basicValidator.validateIdRange(reviewId);
        basicValidator.validateIdRange(userId);

        entityValidator.validateUserIsActive(userId);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        if (review.getUserId() != userId) {
            throw new AppException(ErrorCode.REVIEW_FORBIDDEN);
        }

        if (review.getImageUrl() != null && !review.getImageUrl().isBlank()) {
            String key = extractKeyFromImageUrl(review.getImageUrl());
            s3Service.deleteFile(key);
        }

        reviewRepository.delete(review);

        log.trace("Completed deleting review: reviewId={}", reviewId);
    }

    private String extractKeyFromImageUrl(String imageUrl) {
        if (!imageUrl.startsWith(S3_URL_PREFIX)) {
            throw new AppException(ErrorCode.INVALID_IMAGE_URL_FORMAT);
        }
        return imageUrl.substring(S3_URL_PREFIX.length());
    }
}
