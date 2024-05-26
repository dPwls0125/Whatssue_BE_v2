package GDG.whatssue.domain.attendance.Error;

import GDG.whatssue.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum AttendanceErrorCode  implements ErrorCode {

    ATTENDANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Attendance Not Found"),
    ATTENDANCE_ALREADY_ONGOING(HttpStatus.BAD_REQUEST,"The Attendance is Already Ongoing"),
    ATTENDANCE_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST,"The Attendance is Already Completed");

    private final HttpStatus httpStatus;
    private final String message;

}
