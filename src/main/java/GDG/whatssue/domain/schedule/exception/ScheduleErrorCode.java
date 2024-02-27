package GDG.whatssue.domain.schedule.exception;

import GDG.whatssue.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScheduleErrorCode implements ErrorCode {

    SCHEDULE_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "Invalid Schedule Id [%d]");

    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Long scheduleId) {
        return String.format(message, scheduleId);
    }
}
