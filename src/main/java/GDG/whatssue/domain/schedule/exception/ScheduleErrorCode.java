package GDG.whatssue.domain.schedule.exception;

import GDG.whatssue.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScheduleErrorCode implements ErrorCode {

    EX4100("4100", HttpStatus.NOT_FOUND, "존재하지 않는 일정입니다."),

    EX4200("4200", HttpStatus.BAD_REQUEST, "이미 출석 진행중인 일정입니다."),
    EX4201("4201", HttpStatus.BAD_REQUEST, "이미 출석 완료된 일정입니다."),
    EX4202("4202", HttpStatus.BAD_REQUEST, "출석 진행 중인 일정이 아닙니다."),

    EX4300("4300", HttpStatus.BAD_REQUEST, "일정의 날짜 또는 시간이 형식에 맞지 않습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
