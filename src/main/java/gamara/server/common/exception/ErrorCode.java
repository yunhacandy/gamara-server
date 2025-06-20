package gamara.server.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 공통
    INTERNAL_SERVER_ERROR("C-001"),

    //Validation
    INVALID_ID_RANGE("V-001"),
    INVALID_LEVEL_RANGE("V-002"),

    //Store
    STORE_NOT_FOUND("S-001"),
    ALREADY_RECOMMENDED("S-002"),
    STORE_RECOMMEND_NOT_FOUND("S-003"),

    //User
    USER_NOT_FOUND("U-001"),
    USER_ALREADY_DELETED("U-002"),

    //Review
    REVIEW_NOT_FOUND("R-001"),
    REVIEW_FORBIDDEN("R-002"),
    REVIEW_ALREADY_EXISTS("R-003"),
    INVALID_SORT_TYPE("R-004"),

    //Image
    INVALID_IMAGE_FILE_FORMAT("I-001"),
    IMAGE_FILE_DELETE_FAIL("I-002"),
    IMAGE_CONVERT_FAIL("I-003"),
    WEBP_CONVERT_FAIL("I-004"),
    FILE_EXTENSION_FAULT("I-005"),
    FILE_DELETE_FAIL("I-006"),
    IMAGE_UPLOAD_FAIL("I-007"),
    INVALID_IMAGE_URL_FORMAT("I-008"),

    // Jwt
    INVALID_ACCESS_TOKEN("J-001"),
    INVALID_REFRESH_TOKEN("J-002"),
    INVALID_JWT("J-003"),
    EXPIRED_TOKEN("J-004"),
    REFRESH_TOKEN_NOT_FOUND("J-005");

    private final String code;
}
