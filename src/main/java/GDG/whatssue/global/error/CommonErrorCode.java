package GDG.whatssue.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{

    EX0000("0000", HttpStatus.FORBIDDEN, "로그인이 필요한 작업입니다"),
    EX0002("0002", HttpStatus.FORBIDDEN, "해당 리소스에 대한 접근 권한이 없습니다"),

    EX0100("0100", HttpStatus.NOT_FOUND, "잘못된 요청 경로입니다."),

    EX0300("0300", HttpStatus.BAD_REQUEST, "잘못된 요청 메서드입니다."),
    EX0301("0301", HttpStatus.BAD_REQUEST, "요청 데이터가 조건에 부합합니다"),
    EX0302("0302", HttpStatus.BAD_REQUEST, "요청 파라미터의 데이터가 조건에 부합합니다"),
    EX0303("0303", HttpStatus.BAD_REQUEST, "요청 파라미터는 필수입니다."),

    EX0400("0400", HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String Message;
}
