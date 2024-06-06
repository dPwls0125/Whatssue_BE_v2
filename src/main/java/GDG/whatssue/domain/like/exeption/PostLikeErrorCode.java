package GDG.whatssue.domain.like.exeption;

import GDG.whatssue.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum PostLikeErrorCode implements ErrorCode {
    EX10200("10200", HttpStatus.BAD_REQUEST, "이미 좋아요를 한 게시글입니다."),
    EX10201("10201", HttpStatus.BAD_REQUEST, "좋아요가 되어있지 않은 게시글입니다.");
    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
