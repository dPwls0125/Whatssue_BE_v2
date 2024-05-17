package GDG.whatssue.domain.schedule.dto;

import GDG.whatssue.domain.schedule.exception.ScheduleErrorCode;
import GDG.whatssue.global.error.CommonException;
import java.util.regex.Pattern;
import lombok.Getter;

@Getter
public class SearchCond {

    private String q;
    private String sDate;
    private String eDate;

    public SearchCond() {
        q = "";
        sDate = "1900-01-01";
        eDate = "2199-12-31";
    }

    public void validateDateFormat() {
        if (!Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", sDate)
            && Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", eDate)) {
            throw new CommonException(ScheduleErrorCode.INVALID_SCHEDULE_DATE_PATTERN_ERROR);
        }
    }
}
