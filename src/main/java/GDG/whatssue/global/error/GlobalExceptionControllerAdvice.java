package GDG.whatssue.global.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionControllerAdvice {

    @ExceptionHandler(CommonException.class)
        public ResponseEntity<ErrorResult> commonExHandle(CommonException e, HttpServletRequest request) {
        log.warn("common exception= ", e);
        ErrorCode errorCode = e.getErrorCode();
        ErrorResult errorResult = new ErrorResult(errorCode.name(), errorCode.getMessage(), request.getRequestURI());

        return new ResponseEntity<>(errorResult, errorCode.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> allExHandle(Exception e, HttpServletRequest request) {
        log.warn("exception= ", e);
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResult errorResult = new ErrorResult(errorCode.name(), errorCode.getMessage(), request.getRequestURI());

        return new ResponseEntity<>(errorResult, errorCode.getHttpStatus());
    }
}
