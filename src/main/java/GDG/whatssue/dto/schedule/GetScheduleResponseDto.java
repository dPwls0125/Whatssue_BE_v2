package GDG.whatssue.dto.schedule;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetScheduleResponseDto {

    private Long scheduleId;
    private String scheduleName;
    private String scheduleContent;
    private String scheduleDate;
    private String scheduleTime;
}
