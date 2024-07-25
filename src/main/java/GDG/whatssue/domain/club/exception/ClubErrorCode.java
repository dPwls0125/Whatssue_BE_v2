package GDG.whatssue.domain.club.exception;

import GDG.whatssue.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClubErrorCode implements ErrorCode {

    EX3000("3000", HttpStatus.FORBIDDEN, "로그인 유저는 해당 모임의 멤버가 아닙니다."),
    EX3003("3003", HttpStatus.FORBIDDEN, "해당 모임의 관리자만 호출할 수 있습니다."),


    EX3100("3100", HttpStatus.NOT_FOUND, "존재하지 않는 모임입니다."),
    EX3101("3101", HttpStatus.NOT_FOUND, "존재하지 않는 모임 가입 코드입니다"),
    EX3102("3102", HttpStatus.NOT_FOUND, "존재하지 않는 모임 가입신청 내역입니다"),


    EX3200("3200", HttpStatus.BAD_REQUEST, "이미 가입한 모임입니다."),
    EX3201("3201", HttpStatus.BAD_REQUEST, "이미 가입 신청한 모임입니다."),
    EX3202("3202", HttpStatus.BAD_REQUEST, "거절되지 않은 모임 가입신청 내역입니다."),
    EX3203("3203", HttpStatus.BAD_REQUEST, "이미 처리가 완료된 신청 내역입니다."),
    EX3204("3204", HttpStatus.BAD_REQUEST, "아직 처리가 완료되지 않은 신청 내역입니다."),
    EX3205("3205", HttpStatus.BAD_REQUEST, "현재 가입 신청을 받고 있지 않는 모임입니다."),
    EX3206("3206", HttpStatus.BAD_REQUEST, "다른 클럽의 정보를 변경 할 수 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

}
