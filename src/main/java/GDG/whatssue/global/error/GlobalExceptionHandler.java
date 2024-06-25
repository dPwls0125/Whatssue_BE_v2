package GDG.whatssue.global.error;

import GDG.whatssue.global.error.ErrorResult.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonException.class)
        public ResponseEntity<ErrorResult> commonExHandle(CommonException e, HttpServletRequest request) {
        log.warn("common exception= {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        ErrorResult errorResult = ErrorResult.builder()
            .code(errorCode.getCode())
            .message(errorCode.getMessage())
            .path(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResult, errorCode.getHttpStatus());
    }

    //valid 검증 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> methodValidExHandle(MethodArgumentNotValidException e, HttpServletRequest request){
        log.warn("exception= {}", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        ErrorCode errorCode = CommonErrorCode.EX0301;

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<ValidationError> validationErrorList = fieldErrors.stream()
            .map(f -> ValidationError.of(f))
            .collect(Collectors.toList());

        ErrorResult errorResult = ErrorResult.builder()
            .code(errorCode.getCode())
            .message(errorCode.getMessage())
            .path(request.getRequestURI())
            .errors(validationErrorList)
            .build();

        return new ResponseEntity<>(errorResult, errorCode.getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResult> paramValidExHandle(ConstraintViolationException e, HttpServletRequest request) {
        log.warn("exception= {}", e.getMessage());
        ErrorCode errorCode = CommonErrorCode.EX0302;

        ErrorResult errorResult = ErrorResult.builder()
            .code(errorCode.getCode())
            .message(e.getMessage())
            .path(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResult, errorCode.getHttpStatus());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResult> missParamExHandle(MissingServletRequestParameterException e, HttpServletRequest request) {
        log.warn("exception= {}", e.getMessage());
        ErrorCode errorCode = CommonErrorCode.EX0303;

        ErrorResult errorResult = ErrorResult.builder()
            .code(errorCode.getCode())
            .message("필수 파라미터입니다. [" + e.getParameterType() + "] " + e.getParameterName())
            .path(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResult, errorCode.getHttpStatus());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResult> NoStaticResourceExHandle(NoResourceFoundException e, HttpServletRequest request) {
        log.warn("exception= {}", e.getMessage());
        ErrorCode errorCode = CommonErrorCode.EX0100;

        ErrorResult errorResult = ErrorResult.builder()
            .code(errorCode.getCode())
            .message(errorCode.getMessage())
            .path(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResult, errorCode.getHttpStatus());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResult> NoStaticResourceExHandle(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn("exception= {}", e.getMessage());
        ErrorCode errorCode = CommonErrorCode.EX0300;

        ErrorResult errorResult = ErrorResult.builder()
            .code(errorCode.getCode())
            .message(errorCode.getMessage())
            .path(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResult, errorCode.getHttpStatus());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResult> dateTimeParseExHandle(DateTimeParseException e, HttpServletRequest request) {
        log.warn("exception= {}", e.getMessage());
        ErrorCode errorCode = CommonErrorCode.EX0304;

        ErrorResult errorResult = ErrorResult.builder()
            .code(errorCode.getCode())
            .message(errorCode.getMessage())
            .path(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResult, errorCode.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> allExHandle(Exception e, HttpServletRequest request) {
        log.warn("exception= {}", e);
        ErrorCode errorCode = CommonErrorCode.EX0400;
        ErrorResult errorResult = ErrorResult.builder()
            .code(errorCode.getCode())
            .message(errorCode.getMessage())
            .path(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResult, errorCode.getHttpStatus());
    }
}
