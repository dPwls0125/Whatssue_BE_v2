package GDG.whatssue.domain.comment.exception;

import GDG.whatssue.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CommentErrorCode implements ErrorCode {

    EX8100("8100", HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
