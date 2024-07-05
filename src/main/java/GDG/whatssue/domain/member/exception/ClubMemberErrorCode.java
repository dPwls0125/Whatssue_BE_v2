package GDG.whatssue.domain.member.exception;

import GDG.whatssue.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClubMemberErrorCode implements ErrorCode {
    EX2100("2100", HttpStatus.NOT_FOUND, "존재하지 않는 멤버입니다."),

    EX2200("2200", HttpStatus.PRECONDITION_REQUIRED, "멤버 프로필 초기 설정이 필요합니다."),

    EX2201("2201", HttpStatus.BAD_REQUEST, "멤버 프로필 설정은 최초에만 가능합니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
