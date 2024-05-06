package GDG.whatssue.domain.post.exception;

import GDG.whatssue.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

    POST_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "Invalid Post Id");

    private final HttpStatus httpStatus;
    private final String message;
}
