package GDG.whatssue.domain.schedule.exception;

import GDG.whatssue.global.error.CommonException;
import GDG.whatssue.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class NoScheduleException extends CommonException {

    private Long scheduleId;

    public NoScheduleException(ErrorCode errorCode, Long scheduleId) {
        super(errorCode);
        this.scheduleId = scheduleId;
    }
}
