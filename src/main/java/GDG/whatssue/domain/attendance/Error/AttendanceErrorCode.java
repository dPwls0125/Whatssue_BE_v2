package GDG.whatssue.domain.attendance.Error;

import GDG.whatssue.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum AttendanceErrorCode  implements  ErrorCode {

    ATTENDANCE_NOT_FOUND("5100",HttpStatus.NOT_FOUND, "Attendance Not Found"),
    ATTENDANCE_ALREADY_ONGOING("5200",HttpStatus.BAD_REQUEST,"The Attendance is Already Ongoing"),
    ATTENDANCE_ALREADY_COMPLETED("5201",HttpStatus.BAD_REQUEST,"The Attendance is Already Completed"),
    ATTENDANCE_IS_NOT_ONGOING("5202",HttpStatus.BAD_REQUEST,"The Attendance is Not Ongoing"),
    NONE_ATTENDANCE_NUM_ERROR("5203",HttpStatus.BAD_REQUEST, "There are no attendance numbers");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

}


