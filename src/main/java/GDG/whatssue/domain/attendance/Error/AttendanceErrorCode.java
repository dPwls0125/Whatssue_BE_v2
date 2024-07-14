package GDG.whatssue.domain.attendance.Error;

import GDG.whatssue.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum AttendanceErrorCode  implements  ErrorCode {

    EX5100("5100",HttpStatus.NOT_FOUND, "Attendance Not Found"),
    EX5200("5200",HttpStatus.BAD_REQUEST,"The Attendance is Already Ongoing"),
    EX5201("5201",HttpStatus.BAD_REQUEST,"The Attendance is Already Completed"),
    EX5202("5202",HttpStatus.BAD_REQUEST,"The Attendance is Not Ongoing"),
    EX5203("5203",HttpStatus.BAD_REQUEST, "There are no attendance numbers, Map is Null"),
    EX5204("5204",HttpStatus.BAD_REQUEST, "Attendance Number is not valid"),
    EX5205("5205",HttpStatus.BAD_REQUEST, "해당 스케줄에 대해 이미 출석한 멤버입니다."),
    EX5206("5206",HttpStatus.INTERNAL_SERVER_ERROR, "출석번호 저장에 실패했습니다."),
    EX5207("5207",HttpStatus.BAD_REQUEST, "존재하지 않는 출석 타입입니다."),
    Ex5208("5208",HttpStatus.BAD_REQUEST, "출석 기록이 존재하지 않는 유저가 존재합니다."),
    Ex5209("5209",HttpStatus.BAD_REQUEST, "아직 출석이 완료되지 않은 스케줄입니다. 출석 정정은 해당 스케줄의 출석 완료 후에 가능합니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

}


