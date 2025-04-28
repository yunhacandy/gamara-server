package gamara.server.service;

import gamara.server.common.exception.AppException;
import gamara.server.common.exception.ErrorCode;
import gamara.server.converter.ReviewDtoConverter;
import gamara.server.domain.entity.Review;
import gamara.server.dto.ReviewCreateRequest;
import gamara.server.repository.ReviewRepository;
import gamara.server.repository.StoreRepository;
import gamara.server.repository.UserRepository;
import java.io.IOException;
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
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final ValidateService validateService;

    @Transactional
    public void registerReview(ReviewCreateRequest request, long userId) {
        validateService.checkValidIdRange(userId);
        validateService.checkValidIdRange(request.storeId());
        validateService.checkValidLevelRange(request.peanutLevel());
        validateService.checkValidLevelRange(request.tingleLevel());

        validateUserExists(userId);
        validateStoreExists(request.storeId());

        String imageUrl = null;
        MultipartFile imageFile = request.image();
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                imageUrl = s3Service.uploadFile(imageFile, IMAGE_DIR);
            } catch (IOException e) {
                log.error("Image upload failed", e);
                throw new AppException(ErrorCode.IMAGE_UPLOAD_FAIL);
            }
        }

        Review review = ReviewDtoConverter.toEntity(userId, request, imageUrl);
        reviewRepository.save(review);

        log.trace("Completed registering review: reviewId={}", review.getId());
    }

    private void validateUserExists(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
    }

    private void validateStoreExists(long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new AppException(ErrorCode.STORE_NOT_FOUND);
        }
    }
}
