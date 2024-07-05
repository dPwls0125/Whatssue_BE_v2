package GDG.whatssue.domain.officialabsence.exception;

import GDG.whatssue.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OfficialAbsenceErrorCode implements ErrorCode {
    EX6100("6102", HttpStatus.NOT_FOUND, "존재하지 않는 공결 신청입니다."),
    EX6200("6200", HttpStatus.BAD_REQUEST, "해당 일정과 멤버에 해당하는 공결 신청이 이미 있습니다."),
    EX6201("6201", HttpStatus.BAD_REQUEST, "자신의 공결 신청만 취소할 수 있습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
