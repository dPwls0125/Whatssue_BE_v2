package GDG.whatssue.domain.schedule.exception;

import GDG.whatssue.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScheduleErrorCode implements ErrorCode {

    SCHEDULE_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "Invalid Schedule Id [%d]"),
    SCHEDULE_DATE_PATTERN_ERROR(HttpStatus.BAD_REQUEST, "Invalid Schedule Date Pattern [yyyy-MM-dd or yyyy-MM]"),
    SCHEDULE_DATETIME_PATTERN_ERROR(HttpStatus.BAD_REQUEST, "Invalid Schedule DateTime Pattern [yyyy-MM-dd HH:ss]");


    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Long scheduleId) {
        return String.format(message, scheduleId);
    }
}
