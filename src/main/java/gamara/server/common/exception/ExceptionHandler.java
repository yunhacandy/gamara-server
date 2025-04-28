package gamara.server.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {
    private static final String LOG_ID = "logId";

    @org.springframework.web.bind.annotation.ExceptionHandler(AppException.class)
    ResponseEntity<ErrorResponse> handleAppException(AppException appException, HttpServletRequest request) {
        log.info("EXCEPTION [{}] [{}] [{}] [{}]",
                request.getAttribute(LOG_ID),
                request.getRequestURI(),
                request.getMethod(),
                appException.getErrorCode().getCode());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ErrorResponse.of(appException.getErrorCode()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ImageException.class)
    ResponseEntity<ErrorResponse> handleImageException(ImageException imageException, HttpServletRequest request) {
        log.info("EXCEPTION [{}] [{}] [{}] [{}]",
                request.getAttribute(LOG_ID),
                request.getRequestURI(),
                request.getMethod(),
                imageException.getErrorCode().getCode());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(imageException.getErrorCode()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleException(Exception exception, HttpServletRequest request) {
        log.error("EXCEPTION [{}] [{}] [{}]",
                request.getAttribute(LOG_ID),
                request.getRequestURI(),
                request.getMethod(),
                exception
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
