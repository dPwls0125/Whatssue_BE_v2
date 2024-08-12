package GDG.whatssue.domain.user.Error;

import GDG.whatssue.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode {

    EX1000("1000", HttpStatus.FORBIDDEN, "본인의 유저 정보만 수정할 수 있습니다."),

    EX1100("1100", HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    EX1101("1101", HttpStatus.NOT_FOUND, "인증번호가 존재하지 않습니다."),
    EX1102("1102", HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),
    EX1103("1103", HttpStatus.NOT_ACCEPTABLE, "인증번호 저장에 실패했습니다.");


    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

}

