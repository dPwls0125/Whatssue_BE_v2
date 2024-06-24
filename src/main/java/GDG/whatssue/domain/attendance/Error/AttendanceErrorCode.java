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
    EX5205("5205",HttpStatus.BAD_REQUEST, "해당 스케줄에 대해 이미 출석한 멤버입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

}


