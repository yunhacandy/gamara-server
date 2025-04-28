package gamara.server.service;

import gamara.server.validator.BasicValidator;
import gamara.server.common.exception.AppException;
import gamara.server.common.exception.ErrorCode;
import gamara.server.common.exception.ImageException;
import gamara.server.converter.ReviewDtoConverter;
import gamara.server.domain.entity.Review;
import gamara.server.dto.ReviewCreateRequest;
import gamara.server.repository.ReviewRepository;
import gamara.server.repository.StoreRepository;
import gamara.server.repository.UserRepository;
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

    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;
    private final BasicValidator basicValidator;
    private final EntityValidator entityValidator;

    @Transactional
    public void registerReview(ReviewCreateRequest request, long userId) throws ImageException {
        basicValidator.checkValidIdRange(userId);
        basicValidator.checkValidIdRange(request.storeId());
        basicValidator.checkValidLevelRange(request.peanutLevel());
        basicValidator.checkValidLevelRange(request.tingleLevel());

        entityValidator.validateUserExists(userId);
        entityValidator.validateStoreExists(request.storeId());

        String imageUrl = null;
        MultipartFile imageFile = request.image();
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = s3Service.uploadFile(imageFile, IMAGE_DIR);
        }

        Review review = ReviewDtoConverter.toEntity(userId, request, imageUrl);
        reviewRepository.save(review);

        log.trace("Completed registering review: reviewId={}", review.getId());
    }
}
