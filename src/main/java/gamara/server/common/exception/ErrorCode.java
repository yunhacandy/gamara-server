package gamara.server.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 공통
    INTERNAL_SERVER_ERROR("C-001"),

    //Image
    INVALID_IMAGE_FILE_FORMAT("I-001"),
    IMAGE_FILE_DELETE_FAIL("I-002"),
    IMAGE_CONVERT_FAIL("I-003"),
    WEBP_CONVERT_FAIL("I-004"),
    FILE_EXTENSION_FAULT("I-005");

    private final String code;
}
