package GDG.whatssue.domain.post.exception;

import GDG.whatssue.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

    EX7100("7100", HttpStatus.NOT_FOUND, "존재하지 않는 게시물입니다."),
    EX7200("7200", HttpStatus.BAD_REQUEST, "공지 게시글 작성 권한이 없습니다."),
    EX7201("7201", HttpStatus.BAD_REQUEST, "공지 게시글 수정 권한이 없습니다."),
    EX7202("7202", HttpStatus.BAD_REQUEST, "공지 게시글 삭제 권한이 없습니다."),
    EX7203("7203", HttpStatus.BAD_REQUEST, "게시글 작성자만 수정 가능합니다."),
    EX7204("7204", HttpStatus.BAD_REQUEST, "게시글 작성자만 삭제 가능합니다."),
    EX7205("7205", HttpStatus.BAD_REQUEST, "이미 좋아요를 한 게시글입니다."),
    EX7206("7206", HttpStatus.BAD_REQUEST, "좋아요가 되어있지 않은 게시글입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
